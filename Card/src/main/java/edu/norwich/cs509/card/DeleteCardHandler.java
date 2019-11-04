package edu.norwich.cs509.card;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.amazonaws.util.json.Jackson;
import com.fasterxml.jackson.databind.JsonNode;

import edu.norwich.cs509.card.db.DeleteCardDB;

public class DeleteCardHandler implements RequestStreamHandler{
	LambdaLogger logger;
	
	boolean deleteCard(String eventtype, String recipient) throws Exception {
		if (logger != null) { logger.log("in DeleteCard"); }
		DeleteCardDB dcd = new DeleteCardDB();
		
		// check if present
		if (dcd.deleteCard(eventtype, recipient)){
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
        
    	String eventtype = "", recipient = "";
    	
    	String param = node.get("eventtype").asText();
    	boolean error = false;
    	
    	eventtype = param;
    	// length==0 or not in preset eventtypes
    	//TODO
    	if (eventtype.length() == 0) {
    		if (logger != null) {logger.log("Unable to parse:" + param + " as eventtype"); }
    		error = true;
    	}
    	
    	param = node.get("recipient").asText();
    	recipient = param;
		if (recipient.length() == 0) {
    		if (logger != null) {logger.log("Unable to parse:" + param + " as recipient"); }
    		error = true;
    	}
		
//		param = node.get("orientation").asText();
//    	orientation = param;
//    	// length==0 or not in preset orientation
//    	//TODO
//		if (orientation.length() == 0) {
//    		if (logger != null) {logger.log("Unable to parse:" + param + " as orientation"); }
//    		error = true;
//    	}  
	    
	    PrintWriter pw = new PrintWriter(output);
	    
	    int statusCode;
		if (error) {
			statusCode = 400;
		} else {
	    	//create card, return 200;
			try {
				if (deleteCard(eventtype, recipient)) {
					statusCode = 200;
				}
				else {
					statusCode = 404;
				}
			} catch (Exception e) {
				logger.log("Unable to delete card -- eventtype:" + eventtype + " e:" + e.getMessage());
				statusCode = 400;
			}
			//if card exists, return 409;
			
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
