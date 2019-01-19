package com.hackalogist.notifier;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackalogist.commons.Constants;
import com.hackalogist.model.MessageModel;

@ServerEndpoint(value = "/ServerEndPoint")
public class ServerEndPoint {

private static final String CURRENT_SESSION_ID = "CURRENT_SESSION_ID";
public static Map<String,Session> users = new HashMap<String,Session>();
	
	@OnOpen
	public void handleOpen(Session userSession) {
		System.out.println("INFO: Adding User: " + userSession.toString() + " to the queue at the server.");
		users.put(userSession.toString(),userSession);
		ServerEndPoint.sendSessionIdToUser(userSession);
		System.out.println("INFO: After Adding number of active users = " + users.size());
	}

	@OnClose
	public void handleClose(Session userSession) {
		System.out.println("INFO: Removing User: " + userSession.toString() + " from the queue at the server.");
		users.remove(userSession);
		System.out.println("INFO: After removing number of active users = " + users.size());
	}

	@OnMessage
	public void handleMessage(String message, Session userSession) {
		System.out.println(message);
	}
	
	public static void sendSessionIdToUser(Session userSession) {
		MessageModel message = new MessageModel();
		message.setKey(Constants.CURRENT_SESSION_ID);
		message.setValue(userSession.toString());
		try {
			userSession.getBasicRemote().sendText(ServerEndPoint.convertToJson(message));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized static void notifyAllUsers() {
		try {
			String message = "";
			if(message != null) {
				for (String eachUserString : users.keySet()) {
					users.get(eachUserString).getBasicRemote().sendText(message);
				}
			}
		} catch (IOException e) {
			System.out.println("ERROR: Server cannot send notifications.");
		}
	}
	
	private static String convertToJson(MessageModel message) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(message);
		} catch (JsonProcessingException e) {
			System.out.println("ERROR: Notification message JSON Parsing Failed.");
		}
		return null;
	}
}
