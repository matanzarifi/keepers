package keepers.nlp.models;

import java.beans.Transient;
import java.sql.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Conversation {
	
	private Long conversationId;
	private String deviceId;
	private Long childId;
	private Long parentId;
	private String appName;
	private String birthDate;
	private Long timeOfConversation;
	private Location lastKnownLocation;
	private List<String> languages;
	private List<Message> messages;
	
	
	public Conversation () {
		
	}
	
	public Conversation(Long conversationId, String deviceId, Long childId, Long parentId, String appName, 
			String birthDate, Long timeOfConversation, Location lastKnownLocation, List<Message> messages) {
		super();
		this.conversationId = conversationId;
		this.deviceId = deviceId;
		this.childId = childId;
		this.parentId = parentId;
		this.appName = appName;
		this.birthDate = birthDate;
		this.timeOfConversation = timeOfConversation;
		this.lastKnownLocation = lastKnownLocation;
		this.messages = messages;
	}

	public Conversation(Conversation conv) {
		super();
		this.deviceId = conv.getDeviceId();
		this.childId = conv.getChildId();
		this.parentId = conv.getParentId();
		this.appName = conv.getAppName();
		this.birthDate = conv.getBirthDate();
		this.timeOfConversation = conv.getTimeOfConversation();
		this.lastKnownLocation = conv.getLastKnownLocation();
		this.messages = conv.getMessages();
	}

	@Transient
	public Long getConversationId() {
		return conversationId;
	}

	public void setConversationId(Long conversationId) {
		this.conversationId = conversationId;
	}
	
	public Location getLastKnownLocation() {
		return lastKnownLocation;
	}

	public void setLastKnownLocation(Location lastKnownLocation) {
		this.lastKnownLocation = lastKnownLocation;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}
	
	public String getDeviceId() {
		return deviceId;
	}
	
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
	public Long getChildId() {
		return childId;
	}
	
	public void setChildId(Long childId) {
		this.childId = childId;
	}
	
	public Long getParentId() {
		return parentId;
	}
	
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	
	public String getAppName() {
		return appName;
	}
	
	public void setAppName(String appName) {
		this.appName = appName;
	}
	
	public String getBirthDate() {
		return birthDate;
	}
	
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
	
	public Long getTimeOfConversation() {
		return timeOfConversation;
	}
	
	public void setTimeOfConversation(Long timeOfConversation) {
		this.timeOfConversation = timeOfConversation;
	}

	public List<String> getLanguages() {
		return languages;
	}

	public void setLanguages(List<String> languages) {
		this.languages = languages;
	}	
}
