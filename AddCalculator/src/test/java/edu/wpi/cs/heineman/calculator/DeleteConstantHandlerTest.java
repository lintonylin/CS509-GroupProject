package edu.wpi.cs.heineman.calculator;

import java.io.*;

import org.junit.Assert;
import org.junit.Test;

import com.google.gson.Gson;

import edu.wpi.cs.heineman.calculator.http.*;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class DeleteConstantHandlerTest extends LambdaTest {

    @Test
    public void testCreateAndChangeConstant() throws IOException {
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
        
        // now delete
        DeleteConstantRequest dcr = new DeleteConstantRequest("x" + rnd);
        
        ccRequest = new Gson().toJson(dcr);
        jsonRequest = new Gson().toJson(new PostRequest(ccRequest));
        
        input = new ByteArrayInputStream(jsonRequest.getBytes());
        output = new ByteArrayOutputStream();

        new DeleteConstantHandler().handleRequest(input, output, createContext("delete"));

        post = new Gson().fromJson(output.toString(), PostResponse.class);
        DeleteConstantResponse d_resp = new Gson().fromJson(post.body, DeleteConstantResponse.class);
        Assert.assertEquals("x" + rnd, d_resp.name);
        
        // try again and this should fail
        input = new ByteArrayInputStream(jsonRequest.getBytes());
        output = new ByteArrayOutputStream();

        new DeleteConstantHandler().handleRequest(input, output, createContext("delete"));
        post = new Gson().fromJson(output.toString(), PostResponse.class);
        d_resp = new Gson().fromJson(post.body, DeleteConstantResponse.class);
        
        Assert.assertEquals(422, d_resp.statusCode);
    }

}
