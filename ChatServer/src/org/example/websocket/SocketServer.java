package org.example.websocket;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value="/chatroomServerEndpoint")
public class SocketServer {
	
	static Set<Session> chatroomUsers = Collections.synchronizedSet(new HashSet<Session>());
	
//	public SocketServer(URI endpointURI) {
//		System.out.println("TESTING");
//		WebSocketContainer container = ContainerProvider.getWebSocketContainer();
//		try {
//			container.connectToServer(this, endpointURI);
//		} catch (DeploymentException | IOException e) {
//			e.printStackTrace();
//		}
//	}
	
	@OnOpen
	public void onOpen(Session userSession) {
		chatroomUsers.add(userSession);
	}
	
	@OnClose
	public void onClose(Session userSession) {
		chatroomUsers.remove(userSession);
	}
	
	@OnMessage
	public void onMessage(String message, Session userSession) {
        System.out.println("On Message for Web Socket");

		String username = (String) userSession.getUserProperties().get("username");
		if (username == null) {
			userSession.getUserProperties().put("username", message);
			try {
				userSession.getBasicRemote().sendText("You are now connected as " + message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			Iterator<Session> chatroomUsersIterator = chatroomUsers.iterator();
			while (chatroomUsersIterator.hasNext())
				try {
					chatroomUsersIterator.next().getBasicRemote().sendText(username + ":" + message);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
		}
	}
}















