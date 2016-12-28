package keepers.nlp.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import keepers.nlp.models.Conversation;
import keepers.nlp.models.Message;

public class TrainingDao {

	// DB connection details
	private static final String URL = "jdbc:postgresql://qdjjtnkv.db.elephantsql.com:5432/jwcpjbje";
	private static final String USER_NAME = "jwcpjbje";
	private static final String PASSWORD = "uDZ9aOC5VXlDF6lVr2EU7WCpVV4oUibB";
	
	// Queries
	private static final String QRY_INSERT_CONVERSTION = "INSERT INTO conversation " + 
			" (device_id,child_id,parent_id,time_of_conversation,platform,birth_date,location_latitude,location_longitude) " + 
			" VALUES(?,?,?,?,?,?,?,?) RETURNING conversation_id";
	private static final String QRY_INSERT_MESSAGES = "INSERT INTO message (conversation_id,message,is_outgoing) VALUES(?,?,?)";
	private static final String QRY_GET_ALL_CONVERSATIONS = "SELECT * FROM conversation";
	private static final String QRY_GET_MESSAGES_BY_CONV_ID = "SELECT * FROM message where conversation_id = ?";
	
	
	private Connection dbConnection;
	
	public TrainingDao() {
		try {
            Class.forName("org.postgresql.Driver");
        }
        catch (java.lang.ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
		
		this.dbConnection = getConnection();
	}
	
	public boolean saveConversation(Conversation conv) {
		try {
			PreparedStatement st = dbConnection.prepareStatement(QRY_INSERT_CONVERSTION);
			setConversationParameters(st, conv);
			Long convId;
			if (st.execute()) {
				ResultSet rs = st.getResultSet();
				rs.next();
				convId = rs.getLong(1);
				saveMessages(conv.getMessages(), convId);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private boolean saveMessages(List<Message> msgs, Long convId) {
		try {
			PreparedStatement st = dbConnection.prepareStatement(QRY_INSERT_MESSAGES);
			for (Message msg : msgs) {
				st.setLong(1, convId);
				st.setString(2, msg.getMsg());
				st.setBoolean(3, msg.isOutgoing());
				st.addBatch();
			}
			st.executeBatch();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public ResultSet getAllConversations() {
		try {
			Statement st = dbConnection.createStatement();
			return st.executeQuery(QRY_GET_ALL_CONVERSATIONS);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public ResultSet getMessagesByConversationId(Long id) {
		try {
			PreparedStatement st = dbConnection.prepareStatement(QRY_GET_MESSAGES_BY_CONV_ID);
			st.setLong(1, id);
			return st.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private void setConversationParameters(PreparedStatement st, Conversation conv) {
		try {
			st.setString(1, conv.getDeviceId());
			st.setLong(2, conv.getChildId());
			st.setLong(3, conv.getParentId());
			st.setLong(4, conv.getTimeOfConversation());
			st.setString(5, conv.getAppName());
			st.setString(6, conv.getBirthDate());
			st.setDouble(7, (conv.getLastKnownLocation() != null) ? conv.getLastKnownLocation().getLatitude() : 0); 
			st.setDouble(8, (conv.getLastKnownLocation() != null) ? conv.getLastKnownLocation().getLongitude() : 0);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private Connection getConnection() {	
		try {
			return DriverManager.getConnection(URL, USER_NAME, PASSWORD);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
