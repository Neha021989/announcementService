package com.amazonaws.lambda.anncouncement;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class AccouncementHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private Map<String, RequestHandler<Map<String, Object>, ApiGatewayResponse>> handlerMap = new HashMap<String, RequestHandler<Map<String, Object>, ApiGatewayResponse>>();

	public AccouncementHandler() {
		handlerMap.put("GET", new CreateAccouncement());
		handlerMap.put("POST", new ListAnnouncement());
	}

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		String requestMethod = (String) input.get("httpMethod");
		if (handlerMap.containsKey(requestMethod)) {
			return handlerMap.get(requestMethod).handleRequest(input, context);
		} else {
			return null;
		}
	}

}
