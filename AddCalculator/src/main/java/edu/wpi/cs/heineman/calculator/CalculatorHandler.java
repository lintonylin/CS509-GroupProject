package edu.wpi.cs.heineman.calculator;

import java.io.*;
import java.util.Scanner;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.google.gson.Gson;

import edu.wpi.cs.heineman.calculator.db.ConstantsDAO;
import edu.wpi.cs.heineman.calculator.http.AddRequest;
import edu.wpi.cs.heineman.calculator.http.AddResponse;
import edu.wpi.cs.heineman.calculator.model.Constant;

/**
 * Final version of calculator.
 * 
 * If using just double values as strings, then returns the result.
 * If any of the strings do not parse as a number, they are searched for as a constant.
 * First we search the RDS database.
 * Second, we attempt to load up from S3 bucket.
 * 
 * Note: I have stopped using com.fasterxml.jackson.databind.JsonNode and instead use two different
 * JSon packages. SimpleJson is just that -- Simple!. GSon is a google package that is quite useful
 * 
 * @author heineman
 */
public class CalculatorHandler implements RequestStreamHandler {

	// I am leaving in this S3 code so it can be a LAST RESORT if the constant is not in the database
	private AmazonS3 s3 = null;

	LambdaLogger logger;

	// This is how you would retrieve a constant, but not something we will do anymore
	double getDoubleFromBucket(String constantName) throws Exception {
		if (s3 == null) {
			s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
		}
		S3Object obj = s3.getObject("f19cs509/constants", constantName);

		try (S3ObjectInputStream constantStream = obj.getObjectContent()) {
			Scanner sc = new Scanner(constantStream);
			String val = sc.nextLine();
			sc.close();
			return Double.parseDouble(val);
		}
	}

	/**
	 * Try to get from RDS first. Then get from bucket.
	 * 
	 * @param arg
	 * @return
	 * @throws Exception
	 */
	public double loadConstant(String arg) throws Exception {
		double val = 0;
		try {
			val = loadValueFromRDS(arg);
			return val;
		} catch (Exception e) {
			return getDoubleFromBucket(arg);
		}
	}

	/** Load from RDS, if it exists
	 * 
	 * @throws Exception 
	 */
	double loadValueFromRDS(String arg) throws Exception {
		if (logger != null) { logger.log("in loadValue"); }
		ConstantsDAO dao = new ConstantsDAO();
		Constant constant = dao.getConstant(arg);
		return constant.value;
	}

	@Override
	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
		logger = context.getLogger();
		logger.log("Loading Java Lambda handler of RequestStreamHandler");

		JSONObject headerJson = new JSONObject();
		headerJson.put("Content-Type",  "application/json");  // not sure if needed anymore?
		headerJson.put("Access-Control-Allow-Methods", "GET,POST,DELETE,OPTIONS");
		headerJson.put("Access-Control-Allow-Origin",  "*");

		JSONObject responseJson = new JSONObject();
		responseJson.put("headers", headerJson);

		AddResponse response = null;

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
			response = new AddResponse(422, "unable to process input");  // unable to process input
			responseJson.put("body", new Gson().toJson(response));
			processed = true;
			body = null;
		}

		if (!processed) {
			AddRequest req = new Gson().fromJson(body, AddRequest.class);
			logger.log(req.toString());

			boolean fail = false;
			String failMessage = "";
			double val1 = 0.0;
			try {
				val1 = Double.parseDouble(req.arg1);
			} catch (NumberFormatException e) {
				try {
					val1 = loadConstant(req.arg1);
				} catch (Exception ex) {
					failMessage = req.arg1 + " is an invalid constant.";
					fail = true;
				}
			}

			double val2 = 0.0;
			try {
				val2 = Double.parseDouble(req.arg2);
			} catch (NumberFormatException e) {
				try {
					val2 = loadConstant(req.arg2);
				} catch (Exception ex) {
					failMessage = req.arg2 + " is an invalid constant.";
					fail = true;
				}
			}

			// compute proper response
			if (fail) {
				response = new AddResponse(400, failMessage);
			} else {
				response = new AddResponse(val1 + val2, 200);  // success
			}
		}

		// last thing we do
		responseJson.put("body", new Gson().toJson(response));  
		responseJson.put("statusCode", response.statusCode);

		logger.log(responseJson.toJSONString());
		OutputStreamWriter writer = new OutputStreamWriter(output, "UTF-8");
		writer.write(responseJson.toJSONString());  
		writer.close();
		
	}
}
