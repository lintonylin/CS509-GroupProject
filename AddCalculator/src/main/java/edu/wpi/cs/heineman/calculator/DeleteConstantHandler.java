package edu.wpi.cs.heineman.calculator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

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
 * Found gson JAR file from
 * https://repo1.maven.org/maven2/com/google/code/gson/gson/2.6.2/gson-2.6.2.jar
 */
public class DeleteConstantHandler implements RequestStreamHandler {

	public LambdaLogger logger = null;

	@Override
	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
		logger = context.getLogger();
		logger.log("Loading Java Lambda handler to delete constant");

		JSONObject headerJson = new JSONObject();
		headerJson.put("Content-Type",  "application/json");  // not sure if needed anymore?
		headerJson.put("Access-Control-Allow-Methods", "DELETE,GET,POST,OPTIONS");
	    headerJson.put("Access-Control-Allow-Origin",  "*");
	        
		JSONObject responseJson = new JSONObject();
		responseJson.put("headers", headerJson);

		DeleteConstantResponse response = null;
		
		// extract body from incoming HTTP DELETE request. If any error, then return 422 error
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
			response = new DeleteConstantResponse("Bad Request:" + pe.getMessage(), 422);  // unable to process input
	        responseJson.put("body", new Gson().toJson(response));
	        processed = true;
	        body = null;
		}

		if (!processed) {
			DeleteConstantRequest req = new Gson().fromJson(body, DeleteConstantRequest.class);
			logger.log(req.toString());

			ConstantsDAO dao = new ConstantsDAO();
			
			// See how awkward it is to call delete with an object, when you only
			// have one part of its information?
			Constant constant = new Constant(req.name, 0);
			
			try {
				if (dao.deleteConstant(constant)) {
					response = new DeleteConstantResponse(req.name, 200);
				} else {
					response = new DeleteConstantResponse(req.name, 422, "Unable to delete constant.");
				}
			} catch (Exception e) {
				response = new DeleteConstantResponse(req.name, 403, "Unable to delete constant: " + req.name + "(" + e.getMessage() + ")");
			}
			
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
