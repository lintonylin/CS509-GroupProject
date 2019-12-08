package edu.norwich.cs509.card;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONObject;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.util.json.Jackson;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.codec.binary.Base64;

import edu.norwich.cs509.card.db.AddImageDB;
import edu.norwich.cs509.card.db.AddTextDB;
import edu.norwich.cs509.card.db.CreateCardDB;
import edu.norwich.cs509.card.db.EditImageDB;

public class EditImageHandler implements RequestStreamHandler {

	LambdaLogger logger;
	
	// To access S3 storage
	private AmazonS3 s3 = null;
			
	// Note: this works, but it would be better to move this to environment/configuration mechanisms
	// which you don't have to do for this project.
	public static final String REAL_BUCKET = "images/";
	public static final String TEST_BUCKET = "testimages/";
	
	/** Store in RDS
	 * 
	 * @throws Exception 
	 */
	boolean editImage(String eventtype, String recipient, String orientation, int left, int top, int width, int height, int eid) throws Exception {
		if (logger != null) { logger.log("in editImage"); }
		
		
		EditImageDB edid = new EditImageDB();
		
		// check if present
		if (edid.editImage(eventtype, recipient, orientation, left, top, width, height, eid)){
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
	    
	    System.out.println(node.toString());
	    System.out.println(node.get("card"));
	    
	    JsonNode card = null;
	    if (node.has("card")) {
        	card = node.get("card");
        }
	    
	    JsonNode position = null;
	    if (node.has("position")) {
	    	position = node.get("position");
        }
	    
    	String eventtype = "", recipient = "", orientation = "";
    	int eid = 0;
    	
    	String param = card.get("eventtype").asText();
    	boolean error = false;
    	
    	eventtype = param;
    	// length==0 or not in preset eventtypes
    	//TODO
    	if (eventtype.length() == 0) {
    		if (logger != null) {logger.log("Unable to parse:" + param + " as eventtype"); }
    		error = true;
    	}
    	
    	param = card.get("recipient").asText();
    	recipient = param;
		if (recipient.length() == 0) {
    		if (logger != null) {logger.log("Unable to parse:" + param + " as recipient"); }
    		error = true;
    	}
		
		param = card.get("orientation").asText();
    	orientation = param;
    	// length==0 or not in preset orientation
    	//TODO
		if (orientation.length() == 0) {
    		if (logger != null) {logger.log("Unable to parse:" + param + " as orientation"); }
    		error = true;
    	}   
		
		param = node.get("eid").asText();
		eid = Integer.valueOf(param);
		
		int left = 0, top = 0, width = 0, height = 0;
		
		int para = Integer.parseInt(position.get("left").asText());
		left = para;
		if (left < 0) {
    		if (logger != null) {logger.log("Unable to parse:" + String.valueOf(para) + " as left position"); }
    		error = true;
    	}
		
		para = Integer.parseInt(position.get("top").asText());
		top = para;
		if (top < 0) {
    		if (logger != null) {logger.log("Unable to parse:" + String.valueOf(para) + " as top position"); }
    		error = true;
    	}
		
		para = Integer.parseInt(position.get("width").asText());
		width = para;
		if (width < 0) {
    		if (logger != null) {logger.log("Unable to parse:" + String.valueOf(para) + " as width position"); }
    		error = true;
    	}
		
		para = Integer.parseInt(position.get("height").asText());
		height = para;
		if (height < 0) {
    		if (logger != null) {logger.log("Unable to parse:" + String.valueOf(para) + " as height position"); }
    		error = true;
    	}
	    
	    PrintWriter pw = new PrintWriter(output);
	    
	    int statusCode = 200;
		if (error) {
			statusCode = 400;
		} else {
				//add to RDS
				try {
					if (editImage(eventtype, recipient, orientation, left, top, width, height, eid)) {
						statusCode = 200;
					}
					else {
						statusCode = 409;
					}
				} catch (Exception e) {
					logger.log("Unable to edit image, " + " e:" + e.getMessage());
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