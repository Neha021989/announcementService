package com.amazonaws.lambda.anncouncement;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.ScanOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class ListAnnouncement implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		AmazonDynamoDB dynamoDb = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.AP_SOUTH_1).build();
		DynamoDB docClient = new DynamoDB(dynamoDb);
		Table table = docClient.getTable("Announcement");
		ItemCollection<ScanOutcome> anncouncements = table.scan();
		List<Announcement> announcements = new ArrayList<Announcement>();
		Iterator<Item> iterator = anncouncements.iterator();
		while (iterator.hasNext()) {
			Item announcement = iterator.next();
			Map announcementMap = announcement.asMap();
			Announcement announcementRetrieved = new Announcement(announcementMap.get("title").toString(),
					announcementMap.get("description").toString(),
					LocalDate.parse(announcementMap.get("date").toString()));
			announcements.add(announcementRetrieved);
		}
		System.out.println("Announcements are " + announcements);
		return ApiGatewayResponse.builder().setStatusCode(200).setObjectBody(announcements)
				.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless")).build();
	}
}
