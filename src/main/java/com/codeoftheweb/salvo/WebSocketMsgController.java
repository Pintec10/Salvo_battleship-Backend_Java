package com.codeoftheweb.salvo;


import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;


@Controller
public class WebSocketMsgController {

    @MessageMapping("/{gameID}")
    @SendTo("/topic/{gameID}")
    public WebSocketMsg sendUpdateAvailableMsg(@DestinationVariable long gameID) throws Exception {
        System.out.println("Message received. Game ID:");
        System.out.println(gameID);
        return new WebSocketMsg("newDataAvailable");

    }

}
