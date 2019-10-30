package edu.wpi.cs.heineman.calculator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Assert;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.Gson;

import edu.wpi.cs.heineman.calculator.http.*;
import edu.wpi.cs.heineman.calculator.model.Constant;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class ListConstantsTest extends LambdaTest {

	
    @Test
    public void testGetList() throws IOException {
    	ListAllConstantsHandler handler = new ListAllConstantsHandler();

    	// no input needed
        String inputString = "{}";
        InputStream input = new ByteArrayInputStream(inputString.getBytes());
        OutputStream output = new ByteArrayOutputStream();

        handler.handleRequest(input, output, createContext("list"));

        PostResponse post = new Gson().fromJson(output.toString(), PostResponse.class);
        AllConstantsResponse resp = new Gson().fromJson(post.body, AllConstantsResponse.class);
        
        boolean hasE = false;
        for (Constant c : resp.list) {
        	if (c.name.equals("e")) { hasE = true; break; }
        }
        Assert.assertTrue("e Needs to exist in the constants table for this test case to work.", hasE);
        Assert.assertEquals(200, resp.statusCode);
    }

}
