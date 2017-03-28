package keepers.nlp.REST;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import keepers.nlp.managers.LanguageAnalysisManager;
import keepers.nlp.models.Conversation;
import keepers.nlp.models.ConversationAnalysisResult;


@Path("/conversation")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class ConversationREST {
	
	LanguageAnalysisManager languageAnalysisManager = LanguageAnalysisManager.getInstance();
	
	public ConversationREST() {
		
	}
	
	@POST
	@Consumes({MediaType.APPLICATION_JSON}) 
	@Produces({MediaType.APPLICATION_JSON})
	public ConversationAnalysisResult submitConversation (Conversation conv) {
		//languageAnalysisManager.loadDictionaryToDB("/Users/user/workspace/keepers-nlp/Dictionary_IT.csv", ",", "DICTIONARY_IT"); // for adding\editing dictionary to DB
		ConversationAnalysisResult result = languageAnalysisManager.analyzeConversation(conv);
		languageAnalysisManager.saveConversation(result);
		return result;
	}
}
