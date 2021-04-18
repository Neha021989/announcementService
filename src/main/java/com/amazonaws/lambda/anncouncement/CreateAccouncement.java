package com.amazonaws.lambda.anncouncement;

import java.util.Collections;
import java.util.Map;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CreateAccouncement implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		try {
			JsonNode body;
			body = new ObjectMapper().readTree((String) input.get("body"));
			AmazonDynamoDB dynamoDb = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.AP_SOUTH_1).build();
			DynamoDB docClient = new DynamoDB(dynamoDb);
			Table table = docClient.getTable("Announcement");
			Item accouncementItem = new Item().withString("title", body.get("title").asText())
					.withString("description", body.get("description").asText())
					.withString("date", body.get("date").asText());
			table.putItem(accouncementItem);
			return ApiGatewayResponse.builder().setStatusCode(200).setRawBody(new String("Saved Successfully!!!"))
					.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless")).build();
		} catch (Exception e) {
			Response responseBody = new Response("Error in creating Announcement: ", input);
			return ApiGatewayResponse.builder().setStatusCode(500).setObjectBody(responseBody)
					.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
					.setBase64Encoded(false).build();
		}
	}
}