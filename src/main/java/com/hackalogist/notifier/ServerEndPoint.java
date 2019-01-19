package com.hackalogist.notifier;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackalogist.commons.Constants;
import org.json.JSONObject;
import org.json.JSONException;
import com.hackalogist.service.GameManagementService;

@ServerEndpoint(value = "/ServerEndPoint")
public class ServerEndPoint {
	
	GameManagementService gameManagementService = new GameManagementService();

public static Map<String,Session> users = new HashMap<String,Session>();
	
	@OnOpen
	public void handleOpen(Session userSession) {
		System.out.println("INFO: Adding User: " + userSession.toString() + " to the queue at the server.");
		users.put(userSession.toString(),userSession);
		//ServerEndPoint.sendResponse(userSession);
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
		Map<String, String> map = null;
		try {
			map = jsonToMap(message);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		gameManagementService.executeOperation(map,userSession.toString());
	}
	
	private static Map<String, String> jsonToMap(String t) throws JSONException {

        HashMap<String, String> map = new HashMap<String, String>();
        JSONObject jObject = new JSONObject(t);
        Iterator<?> keys = jObject.keys();

        while( keys.hasNext() ){
            String key = (String)keys.next();
            String value = jObject.getString(key); 
            map.put(key, value);

        }
        return map;
    }
	
	
	public static void sendResponse(String userSessionString, HashMap<String,List<String>> message) {
		ObjectMapper mapper = new ObjectMapper();
		Session userSession=users.get(userSessionString);
		try {
			userSession.getBasicRemote().sendText(mapper.writeValueAsString((message)));
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
	
	/*private static String convertToJson(MessageModel message) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(message);
		} catch (JsonProcessingException e) {
			System.out.println("ERROR: Notification message JSON Parsing Failed.");
		}
		return null;
	}
	*/
}
