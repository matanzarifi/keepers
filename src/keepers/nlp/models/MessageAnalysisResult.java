package keepers.nlp.models;

import java.util.Collection;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MessageAnalysisResult extends Message {

	private SeverityLevel messageSeverity;
	private String mostSevereWord;
	private Collection<String> severeWords;

	public MessageAnalysisResult () {
		
	}
	
	public MessageAnalysisResult (Message msg) {
		super(msg);
	}
	
	public MessageAnalysisResult (Message msg, SeverityLevel lvl) {
		super(msg);
		this.setMessageSeverity(lvl);
	}
	
	public MessageAnalysisResult (Message msg, SeverityLevel lvl, Collection<String> words) {
		super(msg);
		this.setMessageSeverity(lvl);
		this.setSevereWords(words);
	}
	
	public MessageAnalysisResult (Message msg, SeverityLevel lvl, Collection<String> words, String mostSevereWord) {
		super(msg);
		this.setMessageSeverity(lvl);
		this.setSevereWords(words);
		this.mostSevereWord = mostSevereWord;
	}

	public SeverityLevel getMessageSeverity() {
		return messageSeverity;
	}

	public void setMessageSeverity(SeverityLevel messageSeverity) {
		this.messageSeverity = messageSeverity;
	}

	public Collection<String> getSevereWords() {
		return severeWords;
	}

	public void setSevereWords(Collection<String> severeWords) {
		this.severeWords = severeWords;
	}

	public String getMostSevereWord() {
		return mostSevereWord;
	}

	public void setMostSevereWord(String mostSevereWord) {
		this.mostSevereWord = mostSevereWord;
	}
}