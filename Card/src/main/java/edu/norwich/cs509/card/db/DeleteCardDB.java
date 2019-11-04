package edu.norwich.cs509.card.db;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DeleteCardDB {
	java.sql.Connection conn;

    public DeleteCardDB() {
    	try  {
    		conn = DatabaseUtil.connect();
    	} catch (Exception e) {
    		conn = null;
    	}
    }
    
    public boolean deleteCard(String eventtype, String recipient) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM innodb.Cards WHERE eventtype = ? and recipient = ?;");
            ps.setString(1, eventtype);
            ps.setString(2, recipient);
            ResultSet resultSet = ps.executeQuery();
            
            // card already exists
            if (!resultSet.next()) {
                resultSet.close();
                return false;
            }

            ps = conn.prepareStatement("DELETE FROM innodb.Cards where eventtype =? and recipient = ?;");
            ps.setString(1,  eventtype);
            ps.setString(2,  recipient);
            ps.execute();
            return true;

        } catch (Exception e) {
        	StringWriter stringWriter = new StringWriter();
        	e.printStackTrace(new PrintWriter(stringWriter));
            throw new Exception("Failed to Delete card: " + stringWriter.toString());
        }
    }
}
