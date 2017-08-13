package keepers.nlp.REST;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import keepers.nlp.managers.DictionaryManager;
import keepers.nlp.models.DictionaryLanguages;

@Path("/dictionary")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class DictionaryREST {
	
	DictionaryManager dictionaryManager = DictionaryManager.getInstance();

	@POST
	@Consumes({MediaType.APPLICATION_JSON}) 
	@Produces({MediaType.APPLICATION_JSON})
	public String updateDictionary () {
		// Load dictionary procedure
		dictionaryManager.loadDictionaryToDB("/Users/user/workspace/keepers-nlp/Dictionary_DE.csv", ",", "DICTIONARY_DE");
		return null;
	}
}
