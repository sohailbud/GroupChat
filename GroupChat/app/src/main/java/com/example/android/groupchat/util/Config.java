package com.example.android.groupchat.util;

/**
 * Created by Sohail on 1/19/16.
 */
public interface Config {

    // Web socket url
    String URL_WEBSOCKET =
            "ws://192.168.1.135:8080/ChatServer/chatroomServerEndpoint";

    // Google project id
    String GOOGLE_SENDER_ID = "603620925341";

    // URL to send GCM token to server
    String URL_GCM_SERVLET = "http://192.168.1.135:8080/ChatServer/ResponseServlet";

}
