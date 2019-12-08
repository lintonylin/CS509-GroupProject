package edu.norwich.cs509.card.db;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class DuplicateCardDB {
	java.sql.Connection conn;

    public DuplicateCardDB() {
    	try  {
    		conn = DatabaseUtil.connect();
    	} catch (Exception e) {
    		conn = null;
    	}
    }
    
    public boolean duplicateCard(String eventtype, String recipient, String orientation, String newrecipient) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Cards WHERE eventtype = ? and recipient = ?;");
            ps.setString(1, eventtype);
            ps.setString(2, newrecipient);
            ResultSet resultSet = ps.executeQuery();
            
            // card already exists
            if (resultSet.next()) {
                resultSet.close();
                return false;
            }
            
            ps = conn.prepareStatement("INSERT INTO Cards (eventtype, recipient, orientation) VALUES(?,?,?);");
            ps.setString(1, eventtype);
            ps.setString(2, newrecipient);
            ps.setString(3, orientation);
            ps.execute();

            ps = conn.prepareStatement("INSERT INTO TextElements (left_corner, top, width, height, font, text, page, eventtype, recipient) SELECT left_corner, top, width, height, font, text, page, eventtype, ?  FROM TextElements WHERE eventtype = ? AND recipient = ?;");
            ps.setString(1, newrecipient);
            ps.setString(2, eventtype);
            ps.setString(3, recipient);
            ps.execute();
            
            ps = conn.prepareStatement("INSERT INTO ImageElements (s3ID, left_corner, top, width, height, page, eventtype, recipient) SELECT s3ID, left_corner, top, width, height, page, eventtype, ?  FROM ImageElements WHERE eventtype = ? AND recipient = ?;");
            ps.setString(1, newrecipient);
            ps.setString(2, eventtype);
            ps.setString(3, recipient);
            ps.execute();
            
            return true;

        } catch (Exception e) {
        	StringWriter stringWriter = new StringWriter();
        	e.printStackTrace(new PrintWriter(stringWriter));
            throw new Exception("Failed to duplicate card: " + stringWriter.toString());
        }
    }
}