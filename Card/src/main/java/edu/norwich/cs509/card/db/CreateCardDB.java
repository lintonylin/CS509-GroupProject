package edu.norwich.cs509.card.db;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class CreateCardDB {
	java.sql.Connection conn;

    public CreateCardDB() {
    	try  {
    		conn = DatabaseUtil.connect();
    	} catch (Exception e) {
    		conn = null;
    	}
    }
    
    public boolean addCard(String eventtype, String recipient, String orientation) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM innodb.Cards WHERE eventtype = ? and recipient = ?;");
            ps.setString(1, eventtype);
            ps.setString(2, recipient);
            ResultSet resultSet = ps.executeQuery();
            
            // card already exists
            if (resultSet.next()) {
                resultSet.close();
                return false;
            }

            ps = conn.prepareStatement("INSERT INTO innodb.Cards (eventtype,recipient,orientation) values(?,?,?);");
            ps.setString(1,  eventtype);
            ps.setString(2,  recipient);
            ps.setString(3,  orientation);
            ps.execute();
            return true;

        } catch (Exception e) {
        	StringWriter stringWriter = new StringWriter();
        	e.printStackTrace(new PrintWriter(stringWriter));
            throw new Exception("Failed to insert card: " + stringWriter.toString());
        }
    }
}