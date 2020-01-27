package com.codeoftheweb.salvo;


import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;


@Controller
public class WebSocketMsgController {

    @MessageMapping("/{gameID}") // when the client sends messages here, the method below is executed
    @SendTo("/topic/{gameID}")  // server will send the returned object to this address, which the client subscribed to
    public WebSocketMsg sendUpdateAvailableMsg(@DestinationVariable long gameID) throws Exception {
        System.out.println("Message received. Game ID:");
        System.out.println(gameID);
        return new WebSocketMsg("newDataAvailable");

    }

}
