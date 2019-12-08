package edu.norwich.cs509.card.db;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class EditTextDB {
	java.sql.Connection conn;

    public EditTextDB() {
    	try  {
    		conn = DatabaseUtil.connect();
    	} catch (Exception e) {
    		conn = null;
    	}
    }
    
    public boolean editText(String eventtype, String recipient, String orientation, int left, int top, int width, int height, String text, int eid, String font) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement("UPDATE TextElements SET left_corner = ?, top = ?, width = ?, height = ?, font = ?, text = ? WHERE text_ID = ? ;");
            ps.setInt(1, left);  //user input left
            ps.setInt(2, top);  //user input top
            ps.setInt(3, width);  //user input width
            ps.setInt(4, height);  //user input height
            ps.setString(5, font);  //user choose font
            ps.setString(6, text);  // user input text
            ps.setInt(7, eid);   // the card page 
            ps.execute();
         
            return true;

        } catch (Exception e) {
        	StringWriter stringWriter = new StringWriter();
        	e.printStackTrace(new PrintWriter(stringWriter));
            throw new Exception("Failed to add text: " + stringWriter.toString());
        }
    }
}