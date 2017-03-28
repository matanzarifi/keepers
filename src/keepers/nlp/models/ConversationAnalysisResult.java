package keepers.nlp.models;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ConversationAnalysisResult extends Conversation {
	
	private List<MessageAnalysisResult> analyzedMessages = new ArrayList<MessageAnalysisResult>();
	private SeverityLevel conversationSeverity;
	private String severityOfWord;
	
	public ConversationAnalysisResult () {
		
	}
	
	public ConversationAnalysisResult (Conversation conv) {
		super(conv);
	}
	
	public ConversationAnalysisResult (Conversation conv, SeverityLevel lvl) {
		super(conv);
		this.conversationSeverity = lvl;
	}
	
	public ConversationAnalysisResult (Conversation conv, SeverityLevel lvl, String severityWord) {
		super(conv);
		this.conversationSeverity = lvl;
		this.severityOfWord = severityWord;
	}
	
	public SeverityLevel getConversationSeverity() {
		return conversationSeverity;
	}

	public void setConversationSeverity(SeverityLevel result) {
		this.conversationSeverity = result;
	}

	public String getSeverityOfWord() {
		return severityOfWord;
	}

	public void setSeverityOfWord(String severityOfWord) {
		this.severityOfWord = severityOfWord;
	}

	public List<MessageAnalysisResult> getAnalyzedMessages() {
		return analyzedMessages;
	}

	public void setAnalyzedMessages(List<MessageAnalysisResult> analyzedMessages) {
		this.analyzedMessages = analyzedMessages;
	}

}
