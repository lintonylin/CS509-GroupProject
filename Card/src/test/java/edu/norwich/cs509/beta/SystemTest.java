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

import edu.norwich.cs509.card.AddTextHandler;
import edu.norwich.cs509.card.CreateCardHandler;
import edu.norwich.cs509.card.DeleteCardHandler;
import edu.norwich.cs509.card.DuplicateCardHandler;
import edu.norwich.cs509.card.EditImageHandler;
import edu.norwich.cs509.card.EditTextHandler;
import edu.norwich.cs509.card.GetCardListHandler;
import edu.norwich.cs509.card.GetImageListHandler;
import edu.norwich.cs509.card.ShowCardHandler;
import edu.norwich.cs509.card.ShowCardImageHandler;
/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
/*public class SystemTest extends LambdaTest {

	void CreateCard(String incoming) throws IOException {
    	CreateCardHandler handler = new CreateCardHandler();

        InputStream input = new ByteArrayInputStream(incoming.getBytes());
        OutputStream output = new ByteArrayOutputStream();

        handler.handleRequest(input, output, createContext("compute"));

        JsonNode outputNode = Jackson.fromJsonString(output.toString(), JsonNode.class);
        //Assert.assertEquals(outgoing, body.get("result").asText());
        Assert.assertEquals("200", outputNode.get("statusCode").asText());
    }
    
    void AddText(String incoming) throws IOException {
    	AddTextHandler handler = new AddTextHandler();

        InputStream input = new ByteArrayInputStream(incoming.getBytes());
        OutputStream output = new ByteArrayOutputStream();

        handler.handleRequest(input, output, createContext("compute"));

        JsonNode outputNode = Jackson.fromJsonString(output.toString(), JsonNode.class);
        //Assert.assertEquals(outgoing, body.get("result").asText());
        Assert.assertEquals("200", outputNode.get("statusCode").asText());
    }
    
    void GetCardList(String incoming) throws IOException {
    	GetCardListHandler handler = new GetCardListHandler();
    	
        InputStream input = new ByteArrayInputStream(incoming.getBytes());
        OutputStream output = new ByteArrayOutputStream();

        handler.handleRequest(input, output, createContext("compute"));
        
        JsonNode outputNode = Jackson.fromJsonString(output.toString(), JsonNode.class);       
        JsonNode body = Jackson.fromJsonString(outputNode.get("body").asText(), JsonNode.class);
        //Assert.assertEquals(outgoing, body.get("result").asText());
        Assert.assertEquals("200", outputNode.get("statusCode").asText());
    }
    
    void GetImageList(String incoming) throws IOException {
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
    
    void EditImage(String incoming) throws IOException {
    	EditImageHandler handler = new EditImageHandler();

        InputStream input = new ByteArrayInputStream(incoming.getBytes());
        OutputStream output = new ByteArrayOutputStream();

        handler.handleRequest(input, output, createContext("compute"));

        JsonNode outputNode = Jackson.fromJsonString(output.toString(), JsonNode.class);
        //Assert.assertEquals(outgoing, body.get("result").asText());
        Assert.assertEquals("200", outputNode.get("statusCode").asText());
    }
    
    void EditText(String incoming) throws IOException {
    	EditTextHandler handler = new EditTextHandler();

        InputStream input = new ByteArrayInputStream(incoming.getBytes());
        OutputStream output = new ByteArrayOutputStream();

        handler.handleRequest(input, output, createContext("compute"));

        JsonNode outputNode = Jackson.fromJsonString(output.toString(), JsonNode.class);
        //Assert.assertEquals(outgoing, body.get("result").asText());
        Assert.assertEquals("200", outputNode.get("statusCode").asText());
    }
    
    void showCard(String incoming) throws IOException {
    	ShowCardHandler handler = new ShowCardHandler();
    	
        InputStream input = new ByteArrayInputStream(incoming.getBytes());
        OutputStream output = new ByteArrayOutputStream();

        handler.handleRequest(input, output, createContext("compute"));
        
        JsonNode outputNode = Jackson.fromJsonString(output.toString(), JsonNode.class);       
        JsonNode body = Jackson.fromJsonString(outputNode.get("body").asText(), JsonNode.class);
        JsonNode page0 = Jackson.fromJsonString(body.get("page0").asText(), JsonNode.class);
        System.out.println(page0);
        //Assert.assertEquals(outgoing, body.get("result").asText());
        Assert.assertEquals("200", outputNode.get("statusCode").asText());
    }
    
    void showCardImage(String incoming) throws IOException {
    	ShowCardImageHandler handler = new ShowCardImageHandler();
    	
        InputStream input = new ByteArrayInputStream(incoming.getBytes());
        OutputStream output = new ByteArrayOutputStream();

        handler.handleRequest(input, output, createContext("compute"));
        
        JsonNode outputNode = Jackson.fromJsonString(output.toString(), JsonNode.class);       
        JsonNode body = Jackson.fromJsonString(outputNode.get("body").asText(), JsonNode.class);
        JsonNode page2 = Jackson.fromJsonString(body.get("page2").asText(), JsonNode.class);
        System.out.println(page2);
        //Assert.assertEquals(outgoing, body.get("result").asText());
        Assert.assertEquals("200", outputNode.get("statusCode").asText());
    }
    
    void duplicateCard(String incoming) throws IOException {
    	DuplicateCardHandler handler = new DuplicateCardHandler();

        InputStream input = new ByteArrayInputStream(incoming.getBytes());
        OutputStream output = new ByteArrayOutputStream();

        handler.handleRequest(input, output, createContext("compute"));

        JsonNode outputNode = Jackson.fromJsonString(output.toString(), JsonNode.class);
        //Assert.assertEquals(outgoing, body.get("result").asText());
        Assert.assertEquals("200", outputNode.get("statusCode").asText());
    }

    void deleteCard(String incoming) throws IOException {
    	DeleteCardHandler handler = new DeleteCardHandler();

        InputStream input = new ByteArrayInputStream(incoming.getBytes());
        OutputStream output = new ByteArrayOutputStream();

        handler.handleRequest(input, output, createContext("compute"));

        JsonNode outputNode = Jackson.fromJsonString(output.toString(), JsonNode.class);
        //JsonNode body = Jackson.fromJsonString(outputNode.get("body").asText(), JsonNode.class);
        //Assert.assertEquals(outgoing, body.get("result").asText());
        Assert.assertEquals("200", outputNode.get("statusCode").asText());
    }
    
    
    @Test
    public void testCardSimple() {
        try {
        	String CreateCardInput = "{\"eventtype\": \"Anniversary\", \"recipient\": \"CodingStar\", \"orientation\": \"Landscape\"}";
        	CreateCard(CreateCardInput);
        	String AddText1 = "{\"card\": {    \"eventtype\": \"Anniversary\",    \"recipient\": \"CodingStar\",\"orientation\": \"Landscape\"  },  \"position\": {    \"left\": 1,    \"top\": 2,    \"width\": 3,    \"height\": 4  },  \"text\": \"Hi, this is the first page of the test!\",  \"page\": 0,  \"font\": \"Comic Sans MS\"}";
        	String AddText2 = "{\"card\": {    \"eventtype\": \"Anniversary\",    \"recipient\": \"CodingStar\",\"orientation\": \"Landscape\"  },  \"position\": {    \"left\": 1,    \"top\": 2,    \"width\": 3,    \"height\": 4  },  \"text\": \"Hi, this is the second page of the test!\",  \"page\": 1,  \"font\": \"Comic Sans MS\"}";
        	String AddText3 = "{\"card\": {    \"eventtype\": \"Anniversary\",    \"recipient\": \"CodingStar\",\"orientation\": \"Landscape\"  },  \"position\": {    \"left\": 1,    \"top\": 2,    \"width\": 3,    \"height\": 4  },  \"text\": \"Hi, this is the third page of the test!\",  \"page\": 2,  \"font\": \"Comic Sans MS\"}";
        	AddText(AddText1);
        	AddText(AddText2);
        	AddText(AddText3);
        	String GetCardListInput = "{\"eventtype\": \"Anniversary\", \"recipient\": \"CodingStar\", \"orientation\": \"Landscape\"}";
        	GetCardList(GetCardListInput);
        	String GetImageListInput = "{\"eventtype\": \"Birthday\", \"recipient\": \"Mary H.\", \"orientation\": \"Landscape\"}";
        	GetImageList(GetImageListInput);
        	String EditImageInput = "{\"card\": {    \"eventtype\": \"Birthday\",    \"recipient\": \"Mary H.\",\"orientation\": \"Landscape\"  },  \"position\": {    \"left\": 100,    \"top\": 200,    \"width\": 300,    \"height\": 400  },  \"eid\": 2}";
        	EditImage(EditImageInput);
        	String EditTextInput = "{\"card\": {    \"eventtype\": \"Birthday\",    \"recipient\": \"Mary H.\",\"orientation\": \"Landscape\"  },  \"position\": {    \"left\": 100,    \"top\": 200,    \"width\": 300,    \"height\": 400  },  \"text\": \"Happy Birthday\",  \"eid\": 57,  \"font\": \"Comic Sans MS\"}";
        	EditText(EditTextInput);
        	String showCardInput = "{\"eventtype\": \"Anniversary\", \"recipient\": \"CodingStar\", \"orientation\": \"Landscape\"}";
        	showCard(showCardInput);
        	String showCardImageInput = "{\"eventtype\": \"Birthday\", \"recipient\": \"Mary H.\", \"orientation\": \"Landscape\"}";
        	showCardImage(showCardImageInput);
        	String duplicateCardInput = "{\"card\": {    \"eventtype\": \"Anniversary\",    \"recipient\": \"CodingStar\",\"orientation\": \"Landscape\"  },  \"newrecipient\": \"SportsStar\"}";
        	duplicateCard(duplicateCardInput);
        	String deleteduplicate = "{\"eventtype\": \"Anniversary\", \"recipient\": \"SportsStar\"}";
        	String deleteorigin = "{\"eventtype\": \"Anniversary\", \"recipient\": \"CodingStar\"}";
        	deleteCard(deleteduplicate);
        	deleteCard(deleteorigin);


        	
        } catch (IOException ioe) {
        	Assert.fail("Invalid:" + ioe.getMessage());
        }
    }
    
    
}*/
