package edu.norwich.cs509.card.db;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class EditImageDB {
	java.sql.Connection conn;

    public EditImageDB() {
    	try  {
    		conn = DatabaseUtil.connect();
    	} catch (Exception e) {
    		conn = null;
    	}
    }
    
    public boolean editImage(String eventtype, String recipient, String orientation, int left, int top, int width, int height, int eid) throws Exception {
        try {
        	PreparedStatement ps = conn.prepareStatement("UPDATE ImageElements SET left_corner = ?, top = ?, width = ?, height = ? WHERE image_ID = ?;");
            ps.setInt(1, left);  //user input left
            ps.setInt(2, top);  //user input top
            ps.setInt(3, width);  //user input width
            ps.setInt(4, height);  //user input height
            ps.setInt(5, eid);  // user input text
            ps.execute();
         
            return true;

        } catch (Exception e) {
        	StringWriter stringWriter = new StringWriter();
        	e.printStackTrace(new PrintWriter(stringWriter));
            throw new Exception("Failed to add image: " + stringWriter.toString());
        }
    }
}