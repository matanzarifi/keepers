package keepers.nlp.dao;

import java.sql.*;
import java.util.List;
import keepers.nlp.models.ConversationAnalysisResult;
import keepers.nlp.models.MessageAnalysisResult;

public class LanguageAnalysisDao extends BaseDao {

	//------------------------------- Queries --------------------------------------
	private static final String QRY_INSERT_CONVERSTION = "INSERT INTO conversation " + 
			" (device_id,child_id,parent_id,time_of_conversation,platform,birth_date,loc_latitude,loc_longitude) " + 
			" VALUES(?,?,?,?,?,?,?,?)";
	private static final String QRY_INSERT_MESSAGE = "INSERT INTO message (conversation_id,message,outgoing,severity) VALUES(?,?,?,?)";
	
	//-------------------------------------------------------------------------------
	
	public LanguageAnalysisDao () {
		super();
	}
	
	public boolean saveConversation(ConversationAnalysisResult convResult) {
		Long convId;
		try {
			PreparedStatement st = dbConnection.prepareStatement(QRY_INSERT_CONVERSTION, Statement.RETURN_GENERATED_KEYS);
			setConversationParameters(st, convResult);
			st.executeUpdate();
			
			ResultSet rs = st.getGeneratedKeys();
			if (rs.next()) {
				convId = rs.getLong(1);
				saveMessages(convResult.getAnalyzedMessages(), convId);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private boolean saveMessages(List<MessageAnalysisResult> msgs, Long convId) {
		try {
			PreparedStatement st = dbConnection.prepareStatement(QRY_INSERT_MESSAGE);
			for (MessageAnalysisResult msg : msgs) {
				st.setLong(1, convId);
				st.setString(2, msg.getMsg());
				st.setInt(3, msg.isOutgoing() ? 1 : 0);
				st.setInt(4, msg.getMessageSeverity().ordinal());
				st.addBatch();
			}
			st.executeBatch();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private void setConversationParameters(PreparedStatement st, ConversationAnalysisResult convResult) {
		try {
			st.setString(1, convResult.getDeviceId());
			st.setLong(2, convResult.getChildId());
			st.setLong(3, convResult.getParentId());
			st.setLong(4, convResult.getTimeOfConversation());
			st.setString(5, convResult.getAppName());
			st.setString(6, convResult.getBirthDate());
			st.setDouble(7, (convResult.getLastKnownLocation() != null) ? convResult.getLastKnownLocation().getLatitude() : 0); 
			st.setDouble(8, (convResult.getLastKnownLocation() != null) ? convResult.getLastKnownLocation().getLongitude() : 0);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
