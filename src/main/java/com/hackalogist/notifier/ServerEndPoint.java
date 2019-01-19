package com.hackalogist.notifier;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/ServerEndPoint")
public class ServerEndPoint {

public static Set<Session> users = new HashSet<Session>();
	
	@OnOpen
	public void handleOpen(Session userSession) {
		System.out.println("INFO: Adding User: " + userSession + " to the queue at the server.");
		users.add(userSession);
		System.out.println("INFO: After Adding number of active users = " + users.size());
	}

	@OnClose
	public void handleClose(Session userSession) {
		System.out.println("INFO: Removing User: " + userSession + " from the queue at the server.");
		users.remove(userSession);
		System.out.println("INFO: After removing number of active users = " + users.size());
	}

	@OnMessage
	public void handleMessage(String message, Session userSession) {
		try {
			System.out.println("INFO: Notifying all the active users. JSON_Message: " + message);
			for (Session eachUser : users) {
				System.out.println("INFO: Notification sent from " + userSession + " to "+ eachUser);
				eachUser.getBasicRemote().sendText(message);
			}
		} catch (IOException e) {
			System.out.println("ERROR: Server cannot send notifications.");
		}
	}
	
	@OnError
	public void handleError() {
		System.out.println("Socket");
	}
	public synchronized static void notifyAllUsers() {
		try {
			String message = "";
			if(message != null) {
				for (Session eachUser : users) {
					eachUser.getBasicRemote().sendText(message);
				}
			}
		} catch (IOException e) {
			System.out.println("ERROR: Server cannot send notifications.");
		}
	}
}
