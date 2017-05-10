package keepers.nlp.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BaseDao {
	
		// DB connection details
		private static final String URL = "jdbc:db2://dashdb-txn-small-yp-lon02-71.services.eu-gb.bluemix.net:50000/BLUDB:user=bluadmin;password=NThhNTU0OTU5NjNj";

		// DB connection object
		protected Connection dbConnection;
		
		public BaseDao() {
			try {
				Class.forName("com.ibm.db2.jcc.DB2Driver");
	        }
	        catch (java.lang.ClassNotFoundException e) {
	            System.out.println(e.getMessage());
	        }
			
			this.dbConnection = getConnection();
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
