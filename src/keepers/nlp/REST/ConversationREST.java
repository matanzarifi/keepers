package keepers.nlp.REST;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import keepers.nlp.managers.DictionaryManager;
import keepers.nlp.managers.LanguageAnalysisManager;
import keepers.nlp.models.Conversation;
import keepers.nlp.models.ConversationAnalysisResult;


@Path("/conversation")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class ConversationREST {
	
	LanguageAnalysisManager languageAnalysisManager = LanguageAnalysisManager.getInstance();
	DictionaryManager dictionaryManager = DictionaryManager.getInstance();
	
	public ConversationREST() {
		
	}
	
	@POST
	@Consumes({MediaType.APPLICATION_JSON}) 
	@Produces({MediaType.APPLICATION_JSON})
	public ConversationAnalysisResult submitConversation (Conversation conv) {
		
		// Load dictionary procedure
		//dictionaryManager.loadDictionaryToDB("/Users/user/workspace/keepers-nlp/Dictionary_EN.csv", ",", "DICTIONARY_EN");
		//dictionaryManager.loadDictionaryToDB("/Users/user/workspace/keepers-nlp/Dictionary_HE.csv", ",", "DICTIONARY_HE");
		//dictionaryManager.loadDictionaryToDB("/Users/user/workspace/keepers-nlp/Dictionary_FR.csv", ",", "DICTIONARY_FR");
		//dictionaryManager.loadDictionaryToDB("/Users/user/workspace/keepers-nlp/Dictionary_IT.csv", ",", "DICTIONARY_IT");
		//return null;
		
		// Standard Procedure
		ConversationAnalysisResult result = languageAnalysisManager.analyzeConversation(conv);
		languageAnalysisManager.saveConversation(result);
		return result;
	}
}
