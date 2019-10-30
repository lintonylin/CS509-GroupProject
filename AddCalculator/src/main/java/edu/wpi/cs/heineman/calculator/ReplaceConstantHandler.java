package edu.wpi.cs.heineman.calculator;

import java.io.*;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;

import edu.wpi.cs.heineman.calculator.db.ConstantsDAO;
import edu.wpi.cs.heineman.calculator.http.*;
import edu.wpi.cs.heineman.calculator.model.Constant;

/**
 * Replace a constant value that exists.

 * @author heineman
 */
public class ReplaceConstantHandler implements RequestStreamHandler {

	LambdaLogger logger;
	
	/** Load from RDS, if it exists
	 * 
	 * @throws Exception 
	 */
	boolean replaceConstant(String name, double value) throws Exception {
		if (logger != null) { logger.log("in replacConstant"); }
		ConstantsDAO dao = new ConstantsDAO();
		
		// check if present
		Constant exist = dao.getConstant(name);
		Constant constant = new Constant (name, value);
		if (exist == null) {
			return false;
		} else {
			return dao.updateConstant(constant);
		}
	}
	
	@Override 
	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
		logger = context.getLogger();

		// set up response
		JSONObject headerJson = new JSONObject();
		headerJson.put("Content-Type",  "application/json");  // not sure if needed anymore?
		headerJson.put("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
		headerJson.put("Access-Control-Allow-Origin",  "*");

		JSONObject responseJson = new JSONObject();
		responseJson.put("headers", headerJson);

		ReplaceConstantResponse response = null;

		// extract body from incoming HTTP POST request. If any error, then return 422 error
		String body;
		boolean processed = false;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			JSONParser parser = new JSONParser();
			JSONObject event = (JSONObject) parser.parse(reader);
			logger.log("event:" + event.toJSONString());

			body = (String)event.get("body");
			if (body == null) {
				body = event.toJSONString();  // this is only here to make testing easier
			}
		} catch (ParseException pe) {
			logger.log(pe.toString());
			response = new ReplaceConstantResponse("unable to process input", 422);  // unable to process input
			responseJson.put("body", new Gson().toJson(response));
			processed = true;
			body = null;
		}
		
		if (!processed) {
			ReplaceConstantRequest req = new Gson().fromJson(body, ReplaceConstantRequest.class);
			logger.log(req.toString());

			try {
				double value = Double.valueOf(req.value);
				
				if (replaceConstant(req.name, value)) {
					response = new ReplaceConstantResponse(req.name);
				} else {
					response = new ReplaceConstantResponse(req.name, 422);
				}
			} catch (Exception e) {
				response = new ReplaceConstantResponse("Unable to replace constant: " + req.name + "(" + e.getMessage() + ")", 400);
			}
		}

		// last thing we do
		responseJson.put("body", new Gson().toJson(response));  
		responseJson.put("statusCode", response.httpCode);

		logger.log(responseJson.toJSONString());
		OutputStreamWriter writer = new OutputStreamWriter(output, "UTF-8");
		writer.write(responseJson.toJSONString());  
		writer.close();
	}
}
