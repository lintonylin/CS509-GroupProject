package edu.norwich.cs509.card;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.amazonaws.util.json.Jackson;
import com.fasterxml.jackson.databind.JsonNode;

public class CreateCardHandler implements RequestStreamHandler {

	@Override
	  public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
		LambdaLogger logger = null;
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
        
    	double arg1 = 0, arg2 = 0;
    	
    	String param = node.get("arg1").asText();
    	boolean error = false;
		try {
    		arg1 = Double.parseDouble(param);
    	} catch (NumberFormatException nfe) {
    		if (logger != null) {logger.log("Unable to parse:" + param + " as arg1"); }
    		error = true;
    	}
    	
		param = node.get("arg2").asText();
		try {
    		arg2 = Double.parseDouble(param);
		} catch (NumberFormatException nfe) {
    		if (logger != null) { logger.log("Unable to parse:" + param + " as arg2"); }
    		error = true;
    	}
	    
	    PrintWriter pw = new PrintWriter(output);
	    
	    int statusCode;
        double sum = 0;
		if (error) {
			statusCode = 400;
		} else {
	    	sum = arg1 + arg2; 
	    	statusCode = 200;
		}
		
		// Needed for CORS integration...
		String response = "{ \n" + 
	 				         "  \"isBase64Encoded\" : false, \n" +
	 				         "  \"statusCode\"      : " + statusCode + ", \n" +
	 				         "  \"headers\" : { \n " +
	 		                 "     \"Access-Control-Allow-Origin\" : \"*\", \n" + 
	 				         "     \"Access-Control-Allow-Method\"  : \"GET,POST,OPTIONS\" \n" + 
	 		                 "  }, \n" +
	 				         "  \"body\" : \"" + "{ \\\"result\\\" : \\\"" + sum + "\\\" }" + "\" \n" +
	 				         "}";
	 		
		// write out.
		pw.print(response);
		pw.close();
	  }


}
