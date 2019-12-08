package edu.norwich.cs509.card.db;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class DeleteTextDB {
	java.sql.Connection conn;

    public DeleteTextDB() {
    	try  {
    		conn = DatabaseUtil.connect();
    	} catch (Exception e) {
    		conn = null;
    	}
    }
    
    public boolean deleteText(int text_id) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM TextElements WHERE text_ID = ?;");
            ps.setInt(1, text_id);
            
            ps.execute();
            return true;

        } catch (Exception e) {
        	StringWriter stringWriter = new StringWriter();
        	e.printStackTrace(new PrintWriter(stringWriter));
            throw new Exception("Failed to insert card: " + stringWriter.toString());
        }
    }
}