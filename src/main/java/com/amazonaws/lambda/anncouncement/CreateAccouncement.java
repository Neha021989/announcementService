package com.amazonaws.lambda.anncouncement;

import java.time.LocalDate;
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
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CreateAccouncement implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		try {
			JsonNode body;
			body = new ObjectMapper().readTree((String) input.get("body"));
			Item announcement = createAnnouncement(body);
			Map announcementMap = announcement.asMap();
			Announcement announcementRetrieved = new Announcement(announcementMap.get("title").toString(),
					announcementMap.get("description").toString(),
					LocalDate.parse(announcementMap.get("date").toString()));
			notifyUser(announcementRetrieved);
			return ApiGatewayResponse.builder().setStatusCode(200).setRawBody(new String("Saved Successfully!!!"))
					.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless")).build();
		} catch (Exception e) {
			Response responseBody = new Response("Error in creating Announcement: ", input);
			return ApiGatewayResponse.builder().setStatusCode(500).setObjectBody(responseBody)
					.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
					.setBase64Encoded(false).build();
		}
	}

	private Item createAnnouncement(JsonNode body) {
		AmazonDynamoDB dynamoDb = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.AP_SOUTH_1).build();
		DynamoDB docClient = new DynamoDB(dynamoDb);
		Table table = docClient.getTable("Announcement");
		Item announcementItem = new Item().withString("title", body.get("title").asText())
				.withString("description", body.get("description").asText())
				.withString("date", body.get("date").asText());
		table.putItem(announcementItem);
		return announcementItem;
	}

	private void notifyUser(Announcement announcementRetrieved) {
		AmazonSNS snsClient = AmazonSNSClientBuilder.standard().withRegion(Regions.AP_SOUTH_1).build();
		// To create a topic
		CreateTopicRequest createTopicRequest = new CreateTopicRequest("HolidayAnncouncement");
		CreateTopicResult createTopicResult = snsClient.createTopic(createTopicRequest);
		// To Subscribe an email endpoint to an Amazon SNS topic.
		final SubscribeRequest subscribeRequest = new SubscribeRequest(createTopicResult.getTopicArn(), "email",
				"neha.choudhary@rocketmail.com");
		snsClient.subscribe(subscribeRequest);
		// To publish a msg
		String msg = "Here is the announcement for today: " + announcementRetrieved.getDescription();
		PublishRequest publishRequest = new PublishRequest(createTopicResult.getTopicArn(), msg);
		snsClient.publish(publishRequest);

	}
}