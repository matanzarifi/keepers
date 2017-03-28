package keepers.nlp.dao;

import java.sql.*;
import java.util.List;
import keepers.nlp.models.Conversation;
import keepers.nlp.models.ConversationAnalysisResult;
import keepers.nlp.models.Message;
import keepers.nlp.models.MessageAnalysisResult;

public class TrainingDao {

	// DB connection details
	private static final String URL = "jdbc:db2://dashdb-txn-small-yp-lon02-71.services.eu-gb.bluemix.net:50000/BLUDB:user=bluadmin;password=NThhNTU0OTU5NjNj";
	
	// Queries
	private static final String QRY_INSERT_CONVERSTION = "INSERT INTO conversation " + 
			" (device_id,child_id,parent_id,time_of_conversation,platform,birth_date,loc_latitude,loc_longitude) " + 
			" VALUES(?,?,?,?,?,?,?,?)";
	private static final String QRY_INSERT_MESSAGE = "INSERT INTO message (conversation_id,message,outgoing,severity) VALUES(?,?,?,?)";
	private static final String QRY_INSERT_DICT_ENTRY = "INSERT INTO $tableName " + 
			" (phrase,six_to_eight,nine_to_eleven,twelve_to_forteen,fifteen_plus) " +
			" VALUES(?,?,?,?,?)";
	private static final String QRY_GET_DICTIONARY = "SELECT * FROM $tableName";
	
	// DB connection object
	private Connection dbConnection;
	
	public TrainingDao() {
		try {
			Class.forName("com.ibm.db2.jcc.DB2Driver");
        }
        catch (java.lang.ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
		
		this.dbConnection = getConnection();
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
	
	public boolean loadDictionaryToDB (String[] lines, String tableName, String delimiter) {
		try {
			PreparedStatement st = dbConnection.prepareStatement(QRY_INSERT_DICT_ENTRY.replace("$tableName", tableName));
			for (String line: lines) {
				String[] splitLine = line.split(delimiter);
				st.setString(1, splitLine[0]);
				st.setInt(2, Integer.valueOf(splitLine[1]));
				st.setInt(3, Integer.valueOf(splitLine[2]));
				st.setInt(4, Integer.valueOf(splitLine[3]));
				st.setInt(5, Integer.valueOf(splitLine[4]));
				st.addBatch();
			}
			st.executeBatch();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public ResultSet getDictionary(String tableName) {
		ResultSet result = null;
		try {
			PreparedStatement st = dbConnection.prepareStatement(QRY_GET_DICTIONARY.replace("$tableName", tableName));
			result =  st.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
			return result;
		}
		return result;
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

	private Connection getConnection() {
		try {
			return DriverManager.getConnection(URL);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
