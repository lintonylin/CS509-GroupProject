package edu.norwich.aws.calculator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Assert;
import org.junit.Test;

import com.amazonaws.util.json.Jackson;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class CalculatorHandlerTest {

    private static final String SAMPLE_INPUT_STRING = "{\"arg1\": \"17\", \"arg2\": \"19\"}";
    private static final String RESULT = "36.0";

    @Test
    public void testCalculatorHandler() throws IOException {
        CalculatorHandler handler = new CalculatorHandler();

        InputStream input = new ByteArrayInputStream(SAMPLE_INPUT_STRING.getBytes());;
        OutputStream output = new ByteArrayOutputStream();

        handler.handleRequest(input, output, null);

        // TODO: validate output here if needed.
        String sampleOutputString = output.toString();
        JsonNode outputNode = Jackson.fromJsonString(output.toString(), JsonNode.class);
        Assert.assertEquals(RESULT, outputNode.get("result").asText());
    }
}
