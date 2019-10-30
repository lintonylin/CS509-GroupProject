package edu.wpi.cs.heineman.calculator;

import java.io.*;
import java.util.List;

import org.json.simple.JSONObject;

import com.amazonaws.services.lambda.runtime.*;
import com.google.gson.Gson;

import edu.wpi.cs.heineman.calculator.db.ConstantsDAO;
import edu.wpi.cs.heineman.calculator.http.*;
import edu.wpi.cs.heineman.calculator.model.Constant;

/**
 * Found gson JAR file from
 * https://repo1.maven.org/maven2/com/google/code/gson/gson/2.6.2/gson-2.6.2.jar
 * 
 * Had to increase time out wait in Lambda function test from 15 seconds to 30 seconds
 */
public class ListAllConstantsHandler implements RequestStreamHandler {

	public LambdaLogger logger = null;

	/** Load from RDS, if it exists
	 * 
	 * @throws Exception 
	 */
	List<Constant> getConstants() throws Exception {
		if (logger != null) { logger.log("in getConstants"); }
		ConstantsDAO dao = new ConstantsDAO();
		
		return dao.getAllConstants();
	}
	
	@Override
	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
		logger = context.getLogger();
		logger.log("Loading Java Lambda handler to list all constants");

		JSONObject headerJson = new JSONObject();
		headerJson.put("Content-Type", "application/json");  // not sure if needed anymore?
		headerJson.put("Access-Control-Allow-Methods", "GET,POST,DELETE,OPTIONS");
	    headerJson.put("Access-Control-Allow-Origin",  "*");
	        
		JSONObject responseJson = new JSONObject();
		responseJson.put("headers", headerJson);

		AllConstantsResponse response;
		try {
			List<Constant> list = getConstants();
			response = new AllConstantsResponse(list, 200);
		} catch (Exception e) {
			response = new AllConstantsResponse(403, e.getMessage());
		}

		// compute proper response
        responseJson.put("body", new Gson().toJson(response));  
        responseJson.put("statusCode", response.statusCode);
        
        logger.log("end result:" + responseJson.toJSONString());
        logger.log(responseJson.toJSONString());
        OutputStreamWriter writer = new OutputStreamWriter(output, "UTF-8");
        writer.write(responseJson.toJSONString());  
        writer.close();
	}
}
