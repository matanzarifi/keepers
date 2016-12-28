package keepers.nlp.REST;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import keepers.nlp.managers.TrainingManager;
import keepers.nlp.models.AnalysisResult;
import keepers.nlp.models.Conversation;
import keepers.nlp.models.ConversationAnalysisResult;


@Path("/conversation")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class ConversationREST {

	public ConversationREST() {
		
	}
	
	@POST
	@Consumes({MediaType.APPLICATION_JSON}) 
	@Produces({MediaType.APPLICATION_JSON})
	public ConversationAnalysisResult submitConversation (Conversation conv) {
		TrainingManager manager = new TrainingManager();
		manager.saveConversation(conv);
		
		ConversationAnalysisResult res = new ConversationAnalysisResult(conv, AnalysisResult.HARMFUL);
		return res;
	}

}
