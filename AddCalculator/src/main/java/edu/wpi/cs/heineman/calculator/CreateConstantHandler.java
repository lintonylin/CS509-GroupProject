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
 * Create a new constant and store in S3 bucket.

 * @author heineman
 */
public class CreateConstantHandler implements RequestStreamHandler {

	LambdaLogger logger;
	
	/** Load from RDS, if it exists
	 * 
	 * @throws Exception 
	 */
	boolean createConstant(String name, double value) throws Exception {
		if (logger != null) { logger.log("in createConstant"); }
		ConstantsDAO dao = new ConstantsDAO();
		
		// check if present
		Constant exist = dao.getConstant(name);
		Constant constant = new Constant (name, value);
		if (exist == null) {
			return dao.addConstant(constant);
		} else {
			return false;
		}
	}
	
	@Override 
	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
		logger = context.getLogger();

		// set up response
		JSONObject headerJson = new JSONObject();
		headerJson.put("Content-Type",  "application/json");  // not sure if needed anymore?
		headerJson.put("Access-Control-Allow-Methods", "GET,POST,DELETE,OPTIONS");
		headerJson.put("Access-Control-Allow-Origin",  "*");

		JSONObject responseJson = new JSONObject();
		responseJson.put("headers", headerJson);

		CreateConstantResponse response = null;

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
			response = new CreateConstantResponse("unable to process input", 422);  // unable to process input
			responseJson.put("body", new Gson().toJson(response));
			processed = true;
			body = null;
		}
		
		if (!processed) {
			CreateConstantRequest req = new Gson().fromJson(body, CreateConstantRequest.class);
			logger.log(req.toString());

			try {
				byte[] encoded = java.util.Base64.getDecoder().decode(req.base64EncodedValue);
				String contents = new String(encoded);
				double value = Double.valueOf(contents);
				
				if (createConstant(req.name, value)) {
					response = new CreateConstantResponse(req.name);
				} else {
					response = new CreateConstantResponse(req.name, 422);
				}
			} catch (Exception e) {
				response = new CreateConstantResponse("Unable to create constant: " + req.name + "(" + e.getMessage() + ")", 400);
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
