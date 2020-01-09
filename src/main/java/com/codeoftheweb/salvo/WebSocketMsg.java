package com.codeoftheweb.salvo;


public class WebSocketMsg {

    private String message;

    public WebSocketMsg() {
    }

    public WebSocketMsg(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}