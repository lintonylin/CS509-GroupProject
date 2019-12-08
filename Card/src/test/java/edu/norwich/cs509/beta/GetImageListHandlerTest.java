package edu.norwich.cs509.beta;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import com.amazonaws.util.json.Jackson;
import com.fasterxml.jackson.databind.JsonNode;

import edu.norwich.cs509.card.CreateCardHandler;
import edu.norwich.cs509.card.GetCardListHandler;
import edu.norwich.cs509.card.GetImageListHandler;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class GetImageListHandlerTest extends LambdaTest {

    void testInput(String incoming) throws IOException {
    	GetImageListHandler handler = new GetImageListHandler();
    	

        ArrayList<String> images = handler.getallimages();
        
        InputStream input = new ByteArrayInputStream(incoming.getBytes());
        OutputStream output = new ByteArrayOutputStream();

        handler.handleRequest(input, output, createContext("compute"));
        
        JsonNode outputNode = Jackson.fromJsonString(output.toString(), JsonNode.class);       
        JsonNode body = Jackson.fromJsonString(outputNode.get("body").asText(), JsonNode.class);
        //Assert.assertEquals(outgoing, body.get("result").asText());
        Assert.assertEquals("200", outputNode.get("statusCode").asText());
    }
	
    void testFailInput(String incoming, String outgoing) throws IOException {
    	CreateCardHandler handler = new CreateCardHandler();

        InputStream input = new ByteArrayInputStream(incoming.getBytes());
        OutputStream output = new ByteArrayOutputStream();

        handler.handleRequest(input, output, createContext("compute"));

        JsonNode outputNode = Jackson.fromJsonString(output.toString(), JsonNode.class);
        Assert.assertEquals("400", outputNode.get("statusCode").asText());
    }
	
    
    @Test
    public void testCardSimple() {
    	String SAMPLE_INPUT_STRING = "{\"eventtype\": \"birthday\", \"recipient\": \"Mary.H\", \"orientation\": \"landscape\"}";
        
        try {
        	testInput(SAMPLE_INPUT_STRING);
        } catch (IOException ioe) {
        	Assert.fail("Invalid:" + ioe.getMessage());
        }
    }
    
    /*
    @Test
    public void testCalculatorConstant() {
    	String SAMPLE_INPUT_STRING = "{\"arg1\": \"pi\", \"arg2\": \"19\"}";
        String RESULT = "22.141592635";
        
        try {
        	testInput(SAMPLE_INPUT_STRING, RESULT);
        } catch (IOException ioe) {
        	Assert.fail("Invalid:" + ioe.getMessage());
        }
    }
    
    @Test
    public void testFailInput() {
    	String SAMPLE_INPUT_STRING = "{\"arg1\": \"- GARBAGE -\", \"arg2\": \"10\"}";
        String RESULT = "";
        
        try {
        	testFailInput(SAMPLE_INPUT_STRING, RESULT);
        } catch (IOException ioe) {
        	Assert.fail("Invalid:" + ioe.getMessage());
        }
    }*/
    
}
