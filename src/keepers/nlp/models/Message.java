package keepers.nlp.models;

import java.beans.Transient;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Message {

	private Long conversationId;
	private String msg;
	private Long timeOfMessage;
	private boolean isOutgoing;
	
	public Message () {
		
	}
	
	public Message (Long convId, String msg, boolean isOutgoing) {
		this.conversationId = convId;
		this.msg = msg;
		this.isOutgoing = isOutgoing;
	}
	
	public Message (Message msg) {
		super();
		this.conversationId = msg.conversationId;
		this.msg = msg.getMsg();
		this.timeOfMessage = msg.timeOfMessage;
		this.isOutgoing = msg.isOutgoing;
	}
	
	public String getMsg() {
		return msg;
	}
	
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public boolean isOutgoing() {
		return isOutgoing;
	}
	
	public void setOutgoing(boolean isOutgoing) {
		this.isOutgoing = isOutgoing;
	}
	
	@Transient
	public Long getConversationId() {
		return conversationId;
	}

	public void setConversationId(Long conversationId) {
		this.conversationId = conversationId;
	}

	public Long getTimeOfMessage() {
		return timeOfMessage;
	}

	public void setTimeOfMessage(Long timeOfMessage) {
		this.timeOfMessage = timeOfMessage;
	}
}
