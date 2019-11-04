package edu.norwich.cs509.card.db;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class GetCardListDB {
	java.sql.Connection conn;
	ResultSet resultSet;

    public GetCardListDB() {
    	try  {
    		conn = DatabaseUtil.connect();
    		resultSet = null;
    	} catch (Exception e) {
    		conn = null;
    		resultSet = null;
    	}
    }
    
    public boolean getCards() throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM innodb.Cards;");
            resultSet = ps.executeQuery();
            if(!resultSet.next()) {
            	return false;
            }
            return true;

        } catch (Exception e) {
        	StringWriter stringWriter = new StringWriter();
        	e.printStackTrace(new PrintWriter(stringWriter));
            throw new Exception("Failed to insert card: " + stringWriter.toString());
        }
    }
    
    public ResultSet getResultSet() {
    	return resultSet;
    }
}
