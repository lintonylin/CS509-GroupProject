package edu.wpi.cs.heineman.calculator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Assert;
import org.junit.Test;

import com.amazonaws.util.json.Jackson;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;

import edu.wpi.cs.heineman.calculator.http.CreateConstantRequest;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class CreateConstantHandlerTest extends LambdaTest {

    void testSuccessInput(String incoming) throws IOException {
    	CreateConstantHandler handler = new CreateConstantHandler();

        InputStream input = new ByteArrayInputStream(incoming.getBytes());
        OutputStream output = new ByteArrayOutputStream();

        handler.handleRequest(input, output, createContext("create"));

        JsonNode outputNode = Jackson.fromJsonString(output.toString(), JsonNode.class);
        Assert.assertEquals("200", outputNode.get("statusCode").asText());
    }
	
    void testFailInput(String incoming, String failureCode) throws IOException {
    	CreateConstantHandler handler = new CreateConstantHandler();

        InputStream input = new ByteArrayInputStream(incoming.getBytes());
        OutputStream output = new ByteArrayOutputStream();

        handler.handleRequest(input, output, createContext("create"));

        JsonNode outputNode = Jackson.fromJsonString(output.toString(), JsonNode.class);
        Assert.assertEquals(failureCode, outputNode.get("statusCode").asText());
    }
    
    @Test
    public void testShouldBeDuplicate() {
    	CreateConstantRequest ccr = new CreateConstantRequest("e", "Mi43MTgyODE4Mjg=");
    	//String SAMPLE_INPUT_STRING = "{\"name\": \"e\", \"base64EncodedValue\": \"Mi43MTgyODE4Mjg=\"}";
    	String SAMPLE_INPUT_STRING = new Gson().toJson(ccr);  
        
        try {
        	testFailInput(SAMPLE_INPUT_STRING, "422");
        } catch (IOException ioe) {
        	Assert.fail("Invalid:" + ioe.getMessage());
        }
    }
   
    // NOTE: this proliferates large number of constants! Be mindful
    @Test
    public void testShouldBeOk() {
    	int rndNum = (int)(990*(Math.random()));
    	String var = "throwAway" + rndNum;
    	
    	CreateConstantRequest ccr = new CreateConstantRequest(var, "Mi43MTgyODE4Mjg=");
    	//String SAMPLE_INPUT_STRING = "{\"name\": \"" + var + "\", \"value\": \"Mi43MTgyODE4Mjg=\"}";
        String SAMPLE_INPUT_STRING = new Gson().toJson(ccr);  
        
        try {
        	testSuccessInput(SAMPLE_INPUT_STRING);
        } catch (IOException ioe) {
        	Assert.fail("Invalid:" + ioe.getMessage());
        }
    }
    
    @Test
    public void testFailInput() {
    	int rndNum = (int)(990*(Math.random()));
    	String var = "throwAway" + rndNum;
    	CreateConstantRequest ccr = new CreateConstantRequest(var, "this is not base64 encoded");
    	String SAMPLE_INPUT_STRING =  new Gson().toJson(ccr);  
        
        try {
        	testFailInput(SAMPLE_INPUT_STRING, "400");
        } catch (IOException ioe) {
        	Assert.fail("Invalid:" + ioe.getMessage());
        }
    }
    
    @Test
    public void testGarbageInput() {
    	String SAMPLE_INPUT_STRING = "{\"sdsd\": \"e3\", \"hgfgdfgdfg\": \"this is not a number\"}";
        
        try {
        	testFailInput(SAMPLE_INPUT_STRING, "400");
        } catch (IOException ioe) {
        	Assert.fail("Invalid:" + ioe.getMessage());
        }
    }
    
}
