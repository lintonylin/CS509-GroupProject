package edu.norwich.cs509.card;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.amazonaws.util.json.Jackson;
import com.fasterxml.jackson.databind.JsonNode;
import edu.norwich.cs509.card.db.GetCardListDB;
import edu.norwich.cs509.card.db.GetImageListDB;
import edu.norwich.cs509.card.db.GetTextListDB;

public class ShowCardHandler implements RequestStreamHandler {
	
	LambdaLogger logger;
	ResultSet rs;
	boolean getCardList(String eventtype, String recipient, int page) throws Exception {
		if (logger != null) { logger.log("in showCard"); }
		 GetTextListDB gtl = new GetTextListDB();
		
		// check if present
		if (gtl.getTexts(eventtype, recipient, page)){
			rs = gtl.getResultSet();
			logger.log("page " + String.valueOf(page) + ": getTextListSuccess");
			return true;
		}
		else {
			return false;
		}
	}
	
	ResultSet rs1;
	boolean getImageList(String eventtype, String recipient, int page) throws Exception {
		if (logger != null) { logger.log("in showCard"); }
		 GetImageListDB gil = new GetImageListDB();
		
		// check if present
		if (gil.getImages(eventtype, recipient, page)){
			rs1 = gil.getResultSet();
			logger.log("page " + String.valueOf(page) + ": getImageListSuccess");
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
       	boolean error = false;
    	
       	String eventtype = "", recipient = "";
    	
    	String param = node.get("eventtype").asText();
    	
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

	    
	    PrintWriter pw = new PrintWriter(output);
	    JSONArray page0 = new JSONArray();
	    JSONArray page1 = new JSONArray();
	    JSONArray page2 = new JSONArray();
	    JSONArray page3 = new JSONArray();
	    int statusCode;
		if (error) {
			statusCode = 400;
		} else {
	    	//create card, return 200;
			try {
				if (getCardList(eventtype, recipient, 0)) {
				    ResultSetMetaData metaData = rs.getMetaData();
				    int columnCount = metaData.getColumnCount();
				    int flag =0;
				    while (flag==0) {
				    	JSONObject jsonObj = new JSONObject();
				    	for (int i = 1; i <= columnCount; i++) {
				    		String columnName =metaData.getColumnLabel(i);
				    		String value = rs.getString(columnName);
				    		jsonObj.put(columnName, value);
				    		logger.log(jsonObj.toJSONString());
				        } 
				    	if(!rs.next()) {
				    		flag =-1;
				    	}
				        page0.add(jsonObj); 
				    }
				}
				if (getCardList(eventtype, recipient, 1)) {
					ResultSetMetaData metaData = rs.getMetaData();
				    int columnCount = metaData.getColumnCount();
				    int flag =0;
				    while (flag==0) {
				    	JSONObject jsonObj = new JSONObject();
				    	for (int i = 1; i <= columnCount; i++) {
				    		String columnName =metaData.getColumnLabel(i);
				    		String value = rs.getString(columnName);
				    		jsonObj.put(columnName, value);
				    		logger.log(jsonObj.toJSONString());
				        } 
				    	if(!rs.next()) {
				    		flag =-1;
				    	}
				        page1.add(jsonObj); 
				    }
				}
				 if (getCardList(eventtype, recipient, 2)) {
					 	ResultSetMetaData metaData = rs.getMetaData();
					    int columnCount = metaData.getColumnCount();
					    int flag =0;
					    while (flag==0) {
					    	JSONObject jsonObj = new JSONObject();
					    	for (int i = 1; i <= columnCount; i++) {
					    		String columnName =metaData.getColumnLabel(i);
					    		String value = rs.getString(columnName);
					    		jsonObj.put(columnName, value);
					    		logger.log(jsonObj.toJSONString());
					        } 
					    	if(!rs.next()) {
					    		flag =-1;
					    	}
					        page2.add(jsonObj); 
					    }
				 }
				if (getCardList(eventtype, recipient, 3)) {
						    ResultSetMetaData metaData = rs.getMetaData();
						    int columnCount = metaData.getColumnCount();
						    int flag =0;
						    while (flag==0) {
						    	JSONObject jsonObj = new JSONObject();
						    	for (int i = 1; i <= columnCount; i++) {
						    		String columnName =metaData.getColumnLabel(i);
						    		String value = rs.getString(columnName);
						    		jsonObj.put(columnName, value);
						    		logger.log(jsonObj.toJSONString());
						        } 
						    	if(!rs.next()) {
						    		flag =-1;
						    	}
						        page3.add(jsonObj); 
						    }
				}
				if (getImageList(eventtype, recipient, 0)) {
				    ResultSetMetaData metaData = rs.getMetaData();
				    int columnCount = metaData.getColumnCount();
				    int flag =0;
				    while (flag==0) {
				    	JSONObject jsonObj = new JSONObject();
				    	for (int i = 1; i <= columnCount; i++) {
				    		String columnName =metaData.getColumnLabel(i);
				    		String value = rs.getString(columnName);
				    		jsonObj.put(columnName, value);
				    		logger.log(jsonObj.toJSONString());
				        } 
				    	if(!rs.next()) {
				    		flag =-1;
				    	}
				        page0.add(jsonObj); 
				    }
				}
				if (getImageList(eventtype, recipient, 1)) {
					ResultSetMetaData metaData = rs1.getMetaData();
				    int columnCount = metaData.getColumnCount();
				    int flag =0;
				    while (flag==0) {
				    	JSONObject jsonObj = new JSONObject();
				    	for (int i = 1; i <= columnCount; i++) {
				    		String columnName =metaData.getColumnLabel(i);
				    		String value = rs1.getString(columnName);
				    		jsonObj.put(columnName, value);
				    		logger.log(jsonObj.toJSONString());
				        } 
				    	if(!rs.next()) {
				    		flag =-1;
				    	}
				        page1.add(jsonObj); 
				    }
				}
				 if (getImageList(eventtype, recipient, 2)) {
					 	ResultSetMetaData metaData = rs1.getMetaData();
					    int columnCount = metaData.getColumnCount();
					    int flag =0;
					    while (flag==0) {
					    	JSONObject jsonObj = new JSONObject();
					    	for (int i = 1; i <= columnCount; i++) {
					    		String columnName =metaData.getColumnLabel(i);
					    		String value = rs1.getString(columnName);
					    		jsonObj.put(columnName, value);
					    		logger.log(jsonObj.toJSONString());
					        } 
					    	if(!rs.next()) {
					    		flag =-1;
					    	}
					        page2.add(jsonObj); 
					    }
				 }
				if (getImageList(eventtype, recipient, 3)) {
						    ResultSetMetaData metaData = rs1.getMetaData();
						    int columnCount = metaData.getColumnCount();
						    int flag =0;
						    while (flag==0) {
						    	JSONObject jsonObj = new JSONObject();
						    	for (int i = 1; i <= columnCount; i++) {
						    		String columnName =metaData.getColumnLabel(i);
						    		String value = rs1.getString(columnName);
						    		jsonObj.put(columnName, value);
						    		logger.log(jsonObj.toJSONString());
						        } 
						    	if(!rs.next()) {
						    		flag =-1;
						    	}
						        page3.add(jsonObj); 
						    }
				}
				statusCode = 200;
			} catch (Exception e) {
				statusCode = 400;
			}
			//if card exists, return 409;
			
	    	//statusCode = 200;
		}
		
		// Needed for CORS integration...
		logger.log(page0.toString());
		
		JSONObject text_pages = new JSONObject();
		text_pages.put("page0", page0.toJSONString());
		text_pages.put("page1", page1.toJSONString());
		text_pages.put("page2", page2.toJSONString());
		text_pages.put("page3", page3.toJSONString());
		
		JSONObject headerJson = new JSONObject();
		headerJson.put("Content-Type",  "application/json");  // not sure if needed anymore?
		headerJson.put("Access-Control-Allow-Methods", "GET,POST,DELETE,OPTIONS");
		headerJson.put("Access-Control-Allow-Origin",  "*");
		
		JSONObject responseJson = new JSONObject();
		responseJson.put("headers", headerJson);
		responseJson.put("statusCode", statusCode);
		responseJson.put("body",text_pages.toJSONString());
		pw.print(responseJson.toJSONString());
		pw.close();
	  }
}