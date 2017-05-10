package keepers.nlp.managers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import keepers.nlp.dao.DictionaryDao;
import keepers.nlp.models.DictionaryEntry;

public class DictionaryManager {

	private DictionaryDao dictionaryDao = new DictionaryDao();
	
	// Singleton implementation
	private static DictionaryManager instance = null;
		
	private DictionaryManager() {
		
	}
	
	public static DictionaryManager getInstance() {
		if(instance == null) {
			instance = new DictionaryManager();
		}
		return instance;
	}
	// -------------------------
	
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
		return dictionaryDao.loadDictionaryToDB(linesArray, tableName, delimiter);
	}
	
	public int updateDictionaryEntry (String tableName, DictionaryEntry entry) {
		if (tableName == null || tableName.isEmpty() || entry == null) {
			return 0;
		}
		
		return dictionaryDao.updateDictionaryEntry(tableName, entry);
	}
		
}
