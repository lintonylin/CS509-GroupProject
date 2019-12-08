package edu.norwich.cs509.card;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;

import org.json.simple.JSONObject;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.amazonaws.util.json.Jackson;
import com.fasterxml.jackson.databind.JsonNode;

import edu.norwich.cs509.card.db.CreateCardDB;
import edu.norwich.cs509.card.db.DeleteImageDB;
import edu.norwich.cs509.card.db.DeleteTextDB;

public class DeleteImageHandler implements RequestStreamHandler {

	LambdaLogger logger;
	
	boolean deleteImage(int image_id) throws Exception {
		if (logger != null) { logger.log("in deleteImage"); }
		DeleteImageDB did = new DeleteImageDB();
		
		// check if present
		if (did.deleteImage(image_id)){
			return true;
		}
		else {
			return false;
		}
	}
	
	@Override
	  public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
		logger = null;
    	if (context != null) { logger = context.getLogger(); }

    	
	    // load entire input into a String (since it contains JSON)
	    StringBuilder incoming = new StringBuilder();
	    try (BufferedReader br = new BufferedReader(new InputStreamReader(input))) {
	      String line = null;
	      while ((line = br.readLine()) != null) {
	        incoming.append(line);
	      }
	    }
	    
	    JsonNode node = Jackson.fromJsonString(incoming.toString(), JsonNode.class);
	    if (node.has("body")) {
        	node = Jackson.fromJsonString(node.get("body").asText(), JsonNode.class);
        }
        
    	int image_id = 0;
    	
    	int param = Integer.parseInt(node.get("eid").asText());
    	boolean error = false;
    	
    	image_id = param;
    	// length==0 or not in preset eventtypes
    	//TODO
    	if (image_id < 0) {
    		if (logger != null) {logger.log("Unable to parse:" + param + " as image_id"); }
    		error = true;
    	}
	    
	    PrintWriter pw = new PrintWriter(output);
	    
	    int statusCode;
		if (error) {
			statusCode = 400;
		} else {
	    	//create card, return 200;
			try {
				if (deleteImage(image_id)) {
					statusCode = 200;
				}
				else {
					statusCode = 409;
				}
			} catch (Exception e) {
				logger.log("Unable to delete image, " + " e:" + e.getMessage());
				statusCode = 400;
			}
			
	    	//statusCode = 200;
		}
		
		// Needed for CORS integration...
		String response = "{ \n" + 
	 				         "  \"isBase64Encoded\" : false, \n" +
	 				         "  \"statusCode\"      : " + statusCode + ", \n" +
	 				         "  \"headers\" : { \n " +
	 		                 "     \"Access-Control-Allow-Origin\" : \"*\", \n" + 
	 				         "     \"Access-Control-Allow-Method\"  : \"GET,POST,OPTIONS\" \n" + 
	 		                 "  } \n}";
	 		
		// write out.
		pw.print(response);
		pw.close();
	  }


}