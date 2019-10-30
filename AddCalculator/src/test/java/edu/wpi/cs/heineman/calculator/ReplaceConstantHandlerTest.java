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
import edu.wpi.cs.heineman.calculator.http.CreateConstantResponse;
import edu.wpi.cs.heineman.calculator.http.DeleteConstantRequest;
import edu.wpi.cs.heineman.calculator.http.PostRequest;
import edu.wpi.cs.heineman.calculator.http.PostResponse;
import edu.wpi.cs.heineman.calculator.http.ReplaceConstantRequest;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class ReplaceConstantHandlerTest extends LambdaTest {

    void testSuccessInput(String incoming) throws IOException {
    	ReplaceConstantHandler handler = new ReplaceConstantHandler();

        InputStream input = new ByteArrayInputStream(incoming.getBytes());
        OutputStream output = new ByteArrayOutputStream();

        handler.handleRequest(input, output, createContext("replace"));

        JsonNode outputNode = Jackson.fromJsonString(output.toString(), JsonNode.class);
        Assert.assertEquals("200", outputNode.get("statusCode").asText());
    }
	
    void testFailInput(String incoming, String failureCode) throws IOException {
    	ReplaceConstantHandler handler = new ReplaceConstantHandler();

        InputStream input = new ByteArrayInputStream(incoming.getBytes());
        OutputStream output = new ByteArrayOutputStream();

        handler.handleRequest(input, output, createContext("replace"));

        JsonNode outputNode = Jackson.fromJsonString(output.toString(), JsonNode.class);
        Assert.assertEquals(failureCode, outputNode.get("statusCode").asText());
    }
   
    // NOTE: this proliferates large number of constants! Be mindful
    @Test
    public void testShouldBeOk() throws IOException {
    	// create constant
        int rnd = (int) (Math.random() * 1000000);
        CreateConstantRequest ccr = new CreateConstantRequest("x" + rnd, "Mi43MTgyODE4Mjg=");
        
        String ccRequest = new Gson().toJson(ccr);
        String jsonRequest = new Gson().toJson(new PostRequest(ccRequest));
        
        InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
        OutputStream output = new ByteArrayOutputStream();

        new CreateConstantHandler().handleRequest(input, output, createContext("create"));

        PostResponse post = new Gson().fromJson(output.toString(), PostResponse.class);
        CreateConstantResponse resp = new Gson().fromJson(post.body, CreateConstantResponse.class);
        System.out.println(resp);
        
        Assert.assertEquals("x" + rnd, resp.response);
        
    	// now change value
    	
    	ReplaceConstantRequest rcr = new ReplaceConstantRequest("x" + rnd, "11.987");
    	String SAMPLE_INPUT_STRING = new Gson().toJson(rcr);  
        
        try {
        	testSuccessInput(SAMPLE_INPUT_STRING);
        } catch (IOException ioe) {
        	Assert.fail("Invalid:" + ioe.getMessage());
        }
        
        // now delete constant
        DeleteConstantRequest dcr = new DeleteConstantRequest("x" + rnd);
        
        ccRequest = new Gson().toJson(dcr);
        jsonRequest = new Gson().toJson(new PostRequest(ccRequest));
        
        input = new ByteArrayInputStream(jsonRequest.getBytes());
        output = new ByteArrayOutputStream();

        new DeleteConstantHandler().handleRequest(input, output, createContext("delete"));

    }
    
    // validates that you cannot replace value of non-existing constant
    @Test
    public void testFailInput() {
    	int rndNum = (int)(990*(Math.random()));
    	String var = "never_existing_" + rndNum;
    	ReplaceConstantRequest ccr = new ReplaceConstantRequest(var, "11.11");
    	String SAMPLE_INPUT_STRING =  new Gson().toJson(ccr);  
        
        try {
        	testFailInput(SAMPLE_INPUT_STRING, "422");
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
