package edu.norwich.cs509.card.db;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class DeleteImageDB {
	java.sql.Connection conn;

    public DeleteImageDB() {
    	try  {
    		conn = DatabaseUtil.connect();
    	} catch (Exception e) {
    		conn = null;
    	}
    }
    
    public boolean deleteImage(int image_id) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM ImageElements WHERE image_ID = ?;");
            ps.setInt(1, image_id);
            
            ps.execute();
            return true;

        } catch (Exception e) {
        	StringWriter stringWriter = new StringWriter();
        	e.printStackTrace(new PrintWriter(stringWriter));
            throw new Exception("Failed to insert card: " + stringWriter.toString());
        }
    }
}