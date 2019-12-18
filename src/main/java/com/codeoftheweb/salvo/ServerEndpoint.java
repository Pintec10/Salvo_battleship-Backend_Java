package com.codeoftheweb.salvo;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;

@javax.websocket.server.ServerEndpoint("/serverEndpoint")
public class ServerEndpoint {

    @OnOpen
    public void handleOpen() {
        System.out.println("Connection established!");
    }

    @OnMessage
    public String handleMessage(String message) {
        System.out.println("Received from client: " + message);
        String reply = "echoing " + message;
        System.out.println("Server's reply message will be: " + reply);
    return reply;
    }

    @OnClose
    public void handleClose() {
        System.out.println(("Now disconnecting."));
    }

    @OnError
    public void handleError(Throwable t) {
    t.printStackTrace();
    }
}
