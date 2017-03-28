package keepers.nlp.managers;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import keepers.nlp.dao.TrainingDao;
import keepers.nlp.models.AgeGroups;
import keepers.nlp.models.Conversation;
import keepers.nlp.models.ConversationAnalysisResult;
import keepers.nlp.models.DictionaryLanguages;
import keepers.nlp.models.SeverityLevel;
import keepers.nlp.models.Message;
import keepers.nlp.models.MessageAnalysisResult;

public class LanguageAnalysisManager {
	
	private static final DateFormat DATE_FORMATTER = new SimpleDateFormat("dd/MM/yyyy");

	
	private static final HashMap<DictionaryLanguages, HashMap<AgeGroups,HashMap<String, SeverityLevel>>> DICTIONARIES = 
			new HashMap<DictionaryLanguages, HashMap<AgeGroups,HashMap<String, SeverityLevel>>>();
	
	private static final List<DictionaryLanguages> ALL_LANGUAGES = 
			new ArrayList<DictionaryLanguages>(Arrays.asList(DictionaryLanguages.values()));
	
	// START STATIC BLOCK
	static {
		initializeDictionaries();
		loadDictionariesFromDB(ALL_LANGUAGES);
	}
	
	private static void initializeDictionaries() {
		for (DictionaryLanguages language : DictionaryLanguages.values()) {
			HashMap<AgeGroups,HashMap<String, SeverityLevel>> map = new HashMap<AgeGroups,HashMap<String, SeverityLevel>>();
			for (AgeGroups group : AgeGroups.values()) {
				map.put(group, new HashMap<String, SeverityLevel>());
			}
			DICTIONARIES.put(language, map);
		}
	}
	
	private static void loadDictionariesFromDB(List<DictionaryLanguages> languages) {
		TrainingDao trainingDao = new TrainingDao();
		for (DictionaryLanguages language : languages) {
			ResultSet resultSet = trainingDao.getDictionary(language.getTableName());
			try {
				while (resultSet.next()) {
					DICTIONARIES.get(language).get(AgeGroups._6_TO_8).put(resultSet.getString(2), SeverityLevel.values()[Integer.valueOf(resultSet.getInt(3))]);
					DICTIONARIES.get(language).get(AgeGroups._9_TO_11).put(resultSet.getString(2), SeverityLevel.values()[Integer.valueOf(resultSet.getInt(4))]);
					DICTIONARIES.get(language).get(AgeGroups._12_TO_14).put(resultSet.getString(2), SeverityLevel.values()[Integer.valueOf(resultSet.getInt(5))]);
					DICTIONARIES.get(language).get(AgeGroups._15_PLUS).put(resultSet.getString(2), SeverityLevel.values()[Integer.valueOf(resultSet.getInt(6))]);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	// END STATIC BLOCK
	
	// Singleton implementation
	private static LanguageAnalysisManager instance = null;
	
	private LanguageAnalysisManager() {
		
	}
	
	public static LanguageAnalysisManager getInstance() {
		if(instance == null) {
			instance = new LanguageAnalysisManager();
		}
		return instance;
	}
	// -------------------------
	
	private TrainingDao trainingDao = new TrainingDao();
	
	public boolean saveConversation(ConversationAnalysisResult convResult) {
		return trainingDao.saveConversation(convResult);
	}
	
	public ConversationAnalysisResult analyzeConversation(Conversation conversation) {
		if (conversation == null || conversation.getMessages() == null) {
			return null;
		}
		
		//first we get the relevant dictionary
		HashMap<String,SeverityLevel> relevantDictionaryByAge = getDictionaryByAgeAndLanguage(conversation);
		
		//next we determine the severity of the conversation
		return getConversationAnalysisResult(conversation, relevantDictionaryByAge);
		
	}
	
	public boolean loadDictionaryToDB (String csvFile, String delimiter, String tableName) {
		List<String> lines = new ArrayList<String>();
		String line ="";
		try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
			while ((line = br.readLine()) != null) {
				lines.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		String[] linesArray = new String[lines.size()];
		linesArray = lines.toArray(linesArray);
		return trainingDao.loadDictionaryToDB(linesArray, tableName, delimiter);
	}
	
	private ConversationAnalysisResult getConversationAnalysisResult(Conversation conversation, 
			HashMap<String,SeverityLevel> dictionaryByAge) {
		
		// init counters map
		HashMap<SeverityLevel, Integer> conversationSeverityCounters = new HashMap<SeverityLevel, Integer>();
		conversationSeverityCounters.put(SeverityLevel.EASY, 0);
		conversationSeverityCounters.put(SeverityLevel.MEDIUM, 0);
		conversationSeverityCounters.put(SeverityLevel.HARD, 0);
		conversationSeverityCounters.put(SeverityLevel.CRITICAL, 0);
		
		// go over all messages in conversation and determine their severity
		ConversationAnalysisResult result = new ConversationAnalysisResult(conversation, SeverityLevel.GOOD);
		String severityOfWord = "";
		SeverityLevel maxLevel = SeverityLevel.GOOD;
		for (Message msg : conversation.getMessages()) {
			
			// analyze message and add to result
			MessageAnalysisResult mar = analyzeMessage(msg, dictionaryByAge);
			result.getAnalyzedMessages().add(mar);
			
			// add result to counters
			SeverityLevel severity = mar.getMessageSeverity();
			if (!SeverityLevel.GOOD.equals(severity)) {
				conversationSeverityCounters.put(severity, conversationSeverityCounters.get(severity) + 1);
				if (severity.ordinal() >= maxLevel.ordinal()) {
					maxLevel = severity;
					severityOfWord = mar.getMostSevereWord();
				}
			}
		}
		
		if (!SeverityLevel.GOOD.equals(maxLevel)) {
			if (SeverityLevel.CRITICAL.equals(maxLevel) || SeverityLevel.HARD.equals(maxLevel)) {
				result.setConversationSeverity(maxLevel);
				result.setSeverityOfWord(severityOfWord);
			}
			else if (SeverityLevel.MEDIUM.equals(maxLevel) || SeverityLevel.EASY.equals(maxLevel)) {
				//check for another medium or easy severity
				int mediumAndEasyCount = conversationSeverityCounters.get(SeverityLevel.MEDIUM) + 
										conversationSeverityCounters.get(SeverityLevel.EASY);
				if (mediumAndEasyCount > 1) {
					result.setConversationSeverity(maxLevel);
					result.setSeverityOfWord(severityOfWord);
				}
			}
		}
		return result;
	}
	
	private MessageAnalysisResult analyzeMessage(Message msg, HashMap<String,SeverityLevel> dictionaryByAge) {
		if (msg.getMsg() == null || msg.getMsg().isEmpty()) {
			return new MessageAnalysisResult(msg, SeverityLevel.GOOD);
		}
		
		//strip words from basic punctuation marks
		String[] words = msg.getMsg().toLowerCase().replaceAll("\\.", "").replaceAll("\\,", "").replaceAll("\\?", "").replaceAll("\\!", "")
				.split(" ");
		//run over words in message and map severity level
		HashMap<String, SeverityLevel> severeWordsMap = new HashMap<String, SeverityLevel>();
		for (String word : words) {
			if (dictionaryByAge.containsKey(word)) {
				SeverityLevel slvl = dictionaryByAge.get(word);
				severeWordsMap.put(word, slvl);
			}
		}
		
		//determine message severity by highest severe word
		SeverityLevel messageSeverity = SeverityLevel.GOOD;
		String mostSevereWord = "";
		for (Entry<String, SeverityLevel> entry : severeWordsMap.entrySet()) {
			if (entry.getValue().ordinal() >= messageSeverity.ordinal()) {
				messageSeverity = entry.getValue();
				mostSevereWord = entry.getKey();
			}
		}
		
		return new MessageAnalysisResult(msg, messageSeverity, severeWordsMap.keySet(), mostSevereWord);
	}

	private HashMap<String, SeverityLevel> getDictionaryByAgeAndLanguage(Conversation conversation) {
		HashMap<String, SeverityLevel> dictResult = new HashMap<String,SeverityLevel>();
		
		if (conversation == null) {
			return dictResult;
		}
		
		int age = calculateAgeOfChild(conversation.getBirthDate());
		List<DictionaryLanguages> languages = getLanguagesFromConversation(conversation);
		
		//we have to get both valid inputs to determine the dictionary we need to use
		if (age == 0 || languages == null || languages.isEmpty()) {
			return new HashMap<String, SeverityLevel>();
		}
		
		for (DictionaryLanguages lang : languages) {
			if (age >= 6 && age <= 8) {
				dictResult.putAll(DICTIONARIES.get(lang).get(AgeGroups._6_TO_8));
			}
			else if (age >= 9 && age <= 11) {
				dictResult.putAll(DICTIONARIES.get(lang).get(AgeGroups._9_TO_11));
			}
			else if (age >= 12 && age <= 14) {
				dictResult.putAll(DICTIONARIES.get(lang).get(AgeGroups._12_TO_14));
			}
			else if (age > 14) {
				dictResult.putAll(DICTIONARIES.get(lang).get(AgeGroups._15_PLUS));
			}
		}
		return dictResult;
	}
	
	private List<DictionaryLanguages> getLanguagesFromConversation(Conversation conversation) {
		List<DictionaryLanguages> languages = new ArrayList<DictionaryLanguages>();
		if (conversation.getLanguages() == null || conversation.getMessages().isEmpty()) {
			return languages;
		}
		
		for (String lang : conversation.getLanguages()) {
			for (DictionaryLanguages dlang : DictionaryLanguages.values()) {
				if (dlang.getLanguageCode().equalsIgnoreCase(lang)) {
					languages.add(dlang);
				}
			}
		}
		return languages;
	}

	private int calculateAgeOfChild(String dob) {
		if (dob == null || dob.isEmpty()) {
			return 0;
		}
		Calendar birthDate = Calendar.getInstance();
		Calendar today = Calendar.getInstance();
		try {
			birthDate.setTime(DATE_FORMATTER.parse(dob));
		} catch (ParseException e) {
			return 0;
		}
		return today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);
	}
}
