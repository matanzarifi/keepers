package keepers.nlp.managers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import keepers.nlp.dao.TrainingDao;
import keepers.nlp.models.Conversation;
import keepers.nlp.models.Location;
import keepers.nlp.models.Message;

public class TrainingManager {
	
	private TrainingDao trainingDao;

	public TrainingManager () {
		this.trainingDao = new TrainingDao();
	}
	
	public boolean saveConversation (Conversation conv) {
		return trainingDao.saveConversation(conv);
	}
	
	public List<Conversation> getAllConversations() {
		List<Conversation> result = new ArrayList<Conversation>();
		try {
		ResultSet rs = trainingDao.getAllConversations();
			if (rs != null) {
				result = convertToConversationList(rs);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	private List<Conversation> convertToConversationList(ResultSet rs) throws SQLException {
		List<Conversation> result = new ArrayList<Conversation>();
		while (rs.next()) {
			Long convId = rs.getLong("conversation_id");
			ResultSet messages = trainingDao.getMessagesByConversationId(convId);
			List<Message> msgList = convertToMessagesList(messages);
			
			Conversation conv = new Conversation(rs.getLong("conversation_id"), rs.getString("device_id"), rs.getLong("child_id"), 
					rs.getLong("parent_id"), rs.getString("platform"), rs.getString("birth_date"), rs.getLong("time_of_conversation"), 
					new Location(rs.getDouble("location_latitude"), rs.getDouble("location_longitude")), msgList);
			result.add(conv);
		}
		return result;
	}

	private List<Message> convertToMessagesList(ResultSet messages) throws SQLException {
		List<Message> result = new ArrayList<Message>();
		while (messages.next()) {
			Message msg = new Message(messages.getLong("conversation_id"), messages.getString("message"), messages.getBoolean("is_outgoing"));	
			result.add(msg);
		}
		return result;
	}
}
