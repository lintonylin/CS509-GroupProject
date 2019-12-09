package edu.norwich.cs509.card.db;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class AddImageDB {
	java.sql.Connection conn;

    public AddImageDB() {
    	try  {
    		conn = DatabaseUtil.connect();
    	} catch (Exception e) {
    		conn = null;
    	}
    }
    
    public boolean addImage(String eventtype, String recipient, String orientation, int left, int top, int width, int height, String image, String image_id, int page) throws Exception {
        try {
        	PreparedStatement ps = conn.prepareStatement("INSERT INTO ImageElements (left_corner, top, width, height, s3ID, page, eventtype, recipient) values (?, ?, ?, ?, ?, ?, ?, ?);");
            ps.setInt(1, left);  //user input left
            ps.setInt(2, top);  //user input top
            ps.setInt(3, width);  //user input width
            ps.setInt(4, height);  //user input height
            ps.setString(5, image_id);  // user input text
            ps.setInt(6, page);   // the card page
            ps.setString(7, eventtype);  //the card eventtype
            ps.setString(8, recipient);   // the card recipient    
            ps.execute();
         
            return true;

        } catch (Exception e) {
        	StringWriter stringWriter = new StringWriter();
        	e.printStackTrace(new PrintWriter(stringWriter));
            throw new Exception("Failed to add image: " + stringWriter.toString());
        }
    }
}