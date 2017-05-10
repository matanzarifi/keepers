package keepers.nlp.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import keepers.nlp.models.DictionaryEntry;

public class DictionaryDao extends BaseDao {
	
	// ------------------------------ Queries ----------------------------------
	private static final String QRY_INSERT_DICT_ENTRY = "INSERT INTO $tableName " + 
			" (phrase,six_to_eight,nine_to_eleven,twelve_to_forteen,fifteen_plus) " +
			" VALUES(?,?,?,?,?)";
	private static final String QRY_GET_DICTIONARY = "SELECT * FROM $tableName";
	
	// -------------------------------------------------------------------------

	public DictionaryDao () {
		super();
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
	
	public int updateDictionaryEntry (String tableName, DictionaryEntry entry) {
		return 0;
	}
}
