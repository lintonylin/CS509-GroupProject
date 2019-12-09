package edu.norwich.cs509.card;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.json.Jackson;
import com.fasterxml.jackson.databind.JsonNode;

import edu.norwich.cs509.card.db.GetCardListDB;

public class GetImageListHandler implements RequestStreamHandler {
	
	LambdaLogger logger;
	
	// Note: this works, but it would be better to move this to environment/configuration mechanisms
	// which you don't have to do for this project.
	public static final String REAL_BUCKET = "images";
	public static final String TEST_BUCKET = "testimages";
		
	private AmazonS3 s3 = null;
	
	public ArrayList<String> getallimages(){
		ArrayList<String> images = new ArrayList<String>();
		AmazonS3 s3 = null;
		if (s3 == null) {
			s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
		}
		
		String bucket = REAL_BUCKET;
		boolean useTestDB = System.getenv("TESTING") != null;
		if (useTestDB) {
			bucket = TEST_BUCKET;
		}
		
		// retrieve listing of all objects in the designated bucket
		ListObjectsV2Request listObjectsRequest = new ListObjectsV2Request()
				  .withBucketName("cs509norwichfans")    // top-level bucket
				  .withPrefix(bucket);            // sub-folder declarations here (i.e., a/b/c)
														  
				
		// request the s3 objects in the s3 bucket 'cs3733wpi/constants' -- change based on your bucket name
		ListObjectsV2Result result = s3.listObjectsV2(listObjectsRequest);
		List<S3ObjectSummary> objects = result.getObjectSummaries();
		
		for (S3ObjectSummary os: objects) {
		      String name = os.getKey();
			      // If name ends with slash it is the 'constants/' bucket itself so you skip
		      if (name.endsWith("/")) { continue; }
				
		      S3Object obj = s3.getObject("cs509norwichfans", name);
		    	
		    	try (S3ObjectInputStream constantStream = obj.getObjectContent()) {
					Scanner sc = new Scanner(constantStream);
					String val = sc.nextLine();
					sc.close();
					
					// just grab name *after* the slash. Note this is a SYSTEM constant
					int postSlash = name.indexOf('/');
			    	images.add(name.substring(postSlash+1));
					// sysConstants.add(new Constant(name.substring(postSlash+1), Double.valueOf(val), true));
				} catch (Exception e) {
					System.out.println("Unable to parse contents of " + name);
				}
		    }
		//System.out.println(images.get(0));
		
		return images;
	}
	
	@Override
	 public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
		logger = null;
    	if (context != null) { logger = context.getLogger(); }
		if (s3 == null) {
			logger.log("attach to S3 request");
			s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
			logger.log("attach to S3 succeed");
		}
	    
		String bucket = REAL_BUCKET;
		boolean useTestDB = System.getenv("TESTING") != null;
		if (useTestDB) {
			bucket = TEST_BUCKET;
		}
		
		// retrieve listing of all objects in the designated bucket
		ListObjectsV2Request listObjectsRequest = new ListObjectsV2Request()
				  .withBucketName("cs509norwichfans")    // top-level bucket
				  .withPrefix(bucket);            // sub-folder declarations here (i.e., a/b/c)
												  
		
		// request the s3 objects in the s3 bucket 'cs3733wpi/constants' -- change based on your bucket name
		logger.log("process request");
		ListObjectsV2Result result = s3.listObjectsV2(listObjectsRequest);
		logger.log("process request succeeded");
		List<S3ObjectSummary> objects = result.getObjectSummaries();
		

	    JSONArray a = new JSONArray();
		for (S3ObjectSummary os: objects) {
		      String name = os.getKey();
			  logger.log("S3 found:" + name);

		      // If name ends with slash it is the 'constants/' bucket itself so you skip
		      if (name.endsWith("/")) { continue; }
				
		      S3Object obj = s3.getObject("cs509norwichfans", name);
		    	
		    	//try (S3ObjectInputStream constantStream = obj.getObjectContent()) {
				//	Scanner sc = new Scanner(constantStream);
				//	byte[] val = new byte[200000];
				//	int i = 0;
				//	val[i] = sc.nextByte();
				//	while (sc.hasNextByte()) {
				//		val[++i] = sc.nextByte();
				//	}
				//	sc.close();
					
					// just grab name *after* the slash. Note this is a SYSTEM constant
					int postSlash = name.indexOf('/');
			    	JSONObject jsonObj = new JSONObject();
			    	String image = "https://cs509norwichfans.s3.amazonaws.com/" + bucket + "/" +  name.substring(postSlash+1);
		    		// String image = new String(val);
			    	//jsonObj.put(name.substring(postSlash+1), image);
		    		a.add(image);
					// sysConstants.add(new Constant(name.substring(postSlash+1), Double.valueOf(val), true));
				//} catch (Exception e) {
				//	logger.log("Unable to parse contents of " + name);
				//}
		    }
			
			//return sysConstants;

		boolean error = false;
	    PrintWriter pw = new PrintWriter(output);
	    int statusCode = 200;
	    /*
		if (error) {
			statusCode = 400;
		} else {
	    	//create card, return 200;
			try {
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
			} catch (Exception e) {
				statusCode = 400;
			}
			//if card exists, return 409;
			
	    	//statusCode = 200;
		}*/
		
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
	 		                 " \"body\" :\"{ \"images\" : \n" + 
	 		                 a.toString() +
	 		                 " }\"\n" +
	 		                 " }";
		// write out.
		pw.print(responseJson.toJSONString());
		pw.close();
	  }
}