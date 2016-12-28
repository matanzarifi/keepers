package keepers.nlp.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ConversationAnalysisResult extends Conversation {
	
	private AnalysisResult result;

	public ConversationAnalysisResult () {
		
	}
	
	public ConversationAnalysisResult (Conversation conv, AnalysisResult res) {
		super(conv);
		this.result = res;
	}
	
	public AnalysisResult getResult() {
		return result;
	}

	public void setResult(AnalysisResult result) {
		this.result = result;
	}

}
