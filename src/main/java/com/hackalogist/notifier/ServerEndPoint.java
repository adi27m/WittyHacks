package com.hackalogist.notifier;

import java.io.EOFException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackalogist.commons.Constants;
import com.hackalogist.service.GameManagementService;

@ServerEndpoint(value = "/ServerEndPoint")
public class ServerEndPoint {
	
	GameManagementService gameManagementService = new GameManagementService();

public static Map<String,Session> users = new HashMap<String,Session>();
	
	@OnOpen
	public void handleOpen(Session userSession) {
		System.out.println("INFO: Adding User: " + userSession.toString() + " to the queue at the server.");
		users.put(userSession.toString(),userSession);
		HashMap< String , List<String>> responseMap=new HashMap<>();
		List<String> usersList=new LinkedList<>();
		usersList.add(userSession.toString());
		List<String> commands=new LinkedList<>();
		commands.add( Constants.CURRENT_SESSION_ID);
		responseMap.put(Constants.COMMAND_NAME,commands);
		responseMap.put(Constants.USER_ID, usersList);
		ServerEndPoint.sendResponse(userSession.toString(),responseMap);
		System.out.println("INFO: After Adding number of active users = " + users.size());
	}

	@OnClose
	public void handleClose(Session userSession) {
		System.out.println("INFO: Removing User: " + userSession.toString() + " from the queue at the server.");
		users.remove(userSession.toString());
		System.out.println("INFO: After removing number of active users = " + users.size());
	}

	@OnMessage
	public void handleMessage(String message, Session userSession) {
		
		Map<String, Object> map = null;
		try {
			map = jsonToMap(message);
			gameManagementService.executeOperation(map,userSession.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@OnError
	public void handleError(Throwable t) {
		// Most likely cause is a user closing their browser. Check to see if
	    // the root cause is EOF and if it is ignore it.
	    // Protect against infinite loops.
	    int count = 0;
	    Throwable root = t;
	    while (root.getCause() != null && count < 20) {
	        root = root.getCause();
	        count ++;
	    }
	    if (root instanceof EOFException) {
	        // Assume this is triggered by the user closing their browser and
	        // ignore it.
	    } else {
	        try {
				throw t;
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	}
	
	private static Map<String, Object> jsonToMap(String t) throws JSONException {

        HashMap<String, Object> map = new HashMap<String, Object>();
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
