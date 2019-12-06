package edu.norwich.cs509.card.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseUtil {

	// These are to be configured and NEVER stored in the code.
	// once you retrieve this code, you can update
	public final static String rdsMySqlDatabaseUrl = "cardmakerdb.chpy8wzjpky3.us-east-1.rds.amazonaws.com";
	public final static String dbUsername = "admin";
	public final static String dbPassword = "Norwich123";
		
	public final static String jdbcTag = "jdbc:mysql://";
	public final static String rdsMySqlDatabasePort = "3306";
	public final static String multiQueries = "?allowMultiQueries=true";
	   
	public final static String dbName = "innodb";    // default created from MySQL WorkBench
	public final static String testingName = "tmp";  // used for testing (also default created)
	
	// pooled across all usages.
	static Connection conn;
 
	/**
	 * Singleton access to DB connection to share resources effectively across multiple accesses.
	 */
	protected static Connection connect() throws Exception {
		if (conn != null) { return conn; }
		
		boolean useTestDB = System.getenv("TESTING") != null;
		
		String schemaName = dbName;
		if(useTestDB) {
			schemaName = testingName;
			System.out.println("USE TEST DB:" + useTestDB);
		}
		
		try {
			//System.out.println("start connecting......");
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(
					jdbcTag + rdsMySqlDatabaseUrl + ":" + rdsMySqlDatabasePort + "/" + schemaName + multiQueries,
					dbUsername,
					dbPassword);
			//System.out.println("Database has been connected successfully.");
			return conn;
		} catch (Exception ex) {
			throw new Exception("Failed in database connection");
		}
	}
}
