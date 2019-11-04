package edu.norwich.cs509.card.db;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class GetCardListDB {
	java.sql.Connection conn;

    public GetCardListDB() {
    	try  {
    		conn = DatabaseUtil.connect();
    	} catch (Exception e) {
    		conn = null;
    	}
    }
    
    public int getCard() throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM innodb.Cards;");
            ResultSet resultSet = ps.executeQuery();
            if(resultSet.wasNull()) {
            	return -1;
            }
            return 0;

        } catch (Exception e) {
        	StringWriter stringWriter = new StringWriter();
        	e.printStackTrace(new PrintWriter(stringWriter));
            throw new Exception("Failed to insert card: " + stringWriter.toString());
        }
    }
}
