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

public class GetCardListHandler implements RequestStreamHandler {
	
	LambdaLogger logger;
	ResultSet rs;
	boolean getCardList() throws Exception {
		if (logger != null) { logger.log("in getCardList"); }
		 GetCardListDB ccd = new GetCardListDB();
		
		// check if present
		if (ccd.getCards()){
			rs = ccd.getResultSet();
			logger.log("getCardSuccess");
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
    	

	    
	    PrintWriter pw = new PrintWriter(output);
	    JSONArray a = new JSONArray();
	    int statusCode;
		if (error) {
			statusCode = 400;
		} else {
	    	//create card, return 200;
			try {
				if (getCardList()) {
					statusCode = 200;
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
				        a.add(jsonObj); 
				    }
				}
				else {
					statusCode = 409;
				}
			} catch (Exception e) {
				statusCode = 400;
			}
			//if card exists, return 409;
			
	    	//statusCode = 200;
		}
		
		// Needed for CORS integration...
		logger.log(a.toString());
		
		JSONObject headerJson = new JSONObject();
		headerJson.put("Content-Type",  "application/json");  // not sure if needed anymore?
		headerJson.put("Access-Control-Allow-Methods", "GET,POST,DELETE,OPTIONS");
		headerJson.put("Access-Control-Allow-Origin",  "*");
		
		JSONObject responseJson = new JSONObject();
		responseJson.put("headers", headerJson);
		responseJson.put("statusCode", statusCode);
		responseJson.put("body",a.toJSONString());
		String response = "{ \n" + 
	 				         "  \"isBase64Encoded\" : false, \n" +
	 				         "  \"statusCode\"      : " + statusCode + ", \n" +
	 				         "  \"headers\" : { \n " +
	 		                 "     \"Access-Control-Allow-Origin\" : \"*\", \n" + 
	 				         "     \"Access-Control-Allow-Method\"  : \"GET,POST,OPTIONS\" \n" + 
	 		                 "  },\n" +
	 		                 " \"body\" :\"{ \"cards\" : \n" + 
	 		                 a.toString() +
	 		                 " }\"\n" +
	 		                 " }";
		// write out.
		pw.print(responseJson.toJSONString());
		pw.close();
	  }
}
