package com.amazonaws.lambda.anncouncement;

import java.io.IOException;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ListAnnouncementTest {

	private static Map input;

	@BeforeClass
	public static void createInput() throws IOException {

	}

	private Context createContext() {
		TestContext ctx = new TestContext();

		// TODO: customize your context here if needed.
		ctx.setFunctionName("List anncouncement");

		return ctx;
	}

	@Test
	public void testLambdaFunctionHandler() {
		ListAnnouncement handler = new ListAnnouncement();
		Context ctx = createContext();

		ApiGatewayResponse response = handler.handleRequest(input, ctx);
		String announcement = response.getBody();

		JsonNode body;
		try {
			body = new ObjectMapper().readTree(announcement).get(0);
			Assert.assertNotNull(body.get("title").asText());
			Assert.assertNotNull(body.get("description").asText());
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}