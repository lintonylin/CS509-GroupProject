package edu.norwich.cs509.card.db;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class GetTextListDB {
	java.sql.Connection conn;
	ResultSet resultSet;

    public GetTextListDB() {
    	try  {
    		conn = DatabaseUtil.connect();
    		resultSet = null;
    	} catch (Exception e) {
    		conn = null;
    		resultSet = null;
    	}
    }
    
    public boolean getTexts(String eventtype, String recipient, int page) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM innodb.TextElements WHERE eventtype = ? AND recipient = ? AND page = ?;");
            ps.setString(1, eventtype);  // the card eventtype
            ps.setString(2, recipient);   // the card recipient
            ps.setInt(3, page);      // the card page

            resultSet = ps.executeQuery();
            if(!resultSet.next()) {
            	return false;
            }
            return true;

        } catch (Exception e) {
        	StringWriter stringWriter = new StringWriter();
        	e.printStackTrace(new PrintWriter(stringWriter));
            throw new Exception("Failed to get text: " + stringWriter.toString());
        }
    }
    
    public ResultSet getResultSet() {
    	return resultSet;
    }
}
