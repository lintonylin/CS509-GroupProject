package edu.norwich.cs509.card.db;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class AddTextDB {
	java.sql.Connection conn;

    public AddTextDB() {
    	try  {
    		conn = DatabaseUtil.connect();
    	} catch (Exception e) {
    		conn = null;
    	}
    }
    
    public boolean addText(String eventtype, String recipient, String orientation, int left, int top, int width, int height, String text, int page, String font) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO innodb.TextElements (left_corner, top, width, height, font, text, page, eventtype, recipient) values (?, ?, ?, ?, ?, ?, ?, ?, ?);");
            ps.setInt(1, left);  //user input left
            ps.setInt(2, top);  //user input top
            ps.setInt(3, width);  //user input width
            ps.setInt(4, height);  //user input height
            ps.setString(5, font);  //user choose font
            ps.setString(6, text);  // user input text
            ps.setInt(7, page);   // the card page
            ps.setString(8, eventtype);  //the card eventtype
            ps.setString(9, recipient);   // the card recipient    
            ps.execute();
         
            return true;

        } catch (Exception e) {
        	StringWriter stringWriter = new StringWriter();
        	e.printStackTrace(new PrintWriter(stringWriter));
            throw new Exception("Failed to add text: " + stringWriter.toString());
        }
    }
}