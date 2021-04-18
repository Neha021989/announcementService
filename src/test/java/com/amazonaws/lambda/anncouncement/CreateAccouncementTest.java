package com.amazonaws.lambda.anncouncement;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class CreateAccouncementTest {

	private static Map input;

	@BeforeClass
	public static void createInput() throws IOException {
		// TODO: set up your sample input object here.
		Announcement announcement = new Announcement("announcement2", "holiday announcement", LocalDate.now());
		input = new HashMap();
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		input.put("body", objectMapper.writeValueAsString(announcement));

	}

	private Context createContext() {
		TestContext ctx = new TestContext();
		ctx.setFunctionName("Create Announcement");

		return ctx;
	}

	@Test
	public void testLambdaFunctionHandler() {
		CreateAccouncement handler = new CreateAccouncement();
		Context ctx = createContext();

		ApiGatewayResponse output = handler.handleRequest(input, ctx);
		Assert.assertEquals("Saved Successfully!!!", output.getBody());
	}
}
