package com.amazonaws.lambda.anncouncement;

import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class AccouncementHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		String requestMethod = (String) input.get("httpMethod");
		// String path = (String) input.get("path");
		// input.get("path")

		switch (requestMethod) {
		case "POST":
			return new CreateAccouncement().handleRequest(input, context);
		case "GET":
			return new ListAnnouncement().handleRequest(input, context);
		default:
			return null;
		}
	}

}
