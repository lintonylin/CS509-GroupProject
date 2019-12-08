package edu.norwich.cs509.beta;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import com.amazonaws.util.json.Jackson;
import com.fasterxml.jackson.databind.JsonNode;

import edu.norwich.cs509.card.AddImageHandler;
import edu.norwich.cs509.card.AddTextHandler;
import edu.norwich.cs509.card.CreateCardHandler;
import edu.norwich.cs509.card.EditImageHandler;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class EditImageHandlerTest extends LambdaTest {

    void testInput(String incoming) throws IOException {
    	EditImageHandler handler = new EditImageHandler();

        InputStream input = new ByteArrayInputStream(incoming.getBytes());
        OutputStream output = new ByteArrayOutputStream();

        handler.handleRequest(input, output, createContext("compute"));

        JsonNode outputNode = Jackson.fromJsonString(output.toString(), JsonNode.class);
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
    	String SAMPLE_INPUT_STRING = "{\"card\": {    \"eventtype\": \"Birthday\",    \"recipient\": \"Mary H.\",\"orientation\": \"Landscape\"  },  \"position\": {    \"left\": 100,    \"top\": 200,    \"width\": 300,    \"height\": 400  },  \"eid\": 3}";

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
