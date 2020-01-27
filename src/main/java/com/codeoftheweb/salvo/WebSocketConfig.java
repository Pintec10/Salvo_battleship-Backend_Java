package com.codeoftheweb.salvo;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
            //enable a simple memory-based message broker to carry the WebSocketMsg
            //back to the client on destinations prefixed with "/topic"
        config.setApplicationDestinationPrefixes("/comms");
            //sets the "/comms" prefix for all methods annotated with @MessageMapping
            //here, client will need to send to "/comms/{gameID}"
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/salvo-websocket")
                    //endpoint to which the client needs to connect for handshake
                .setAllowedOrigins("http://localhost:8081",
                        "chrome-extension://ggnhohnkfcpcanfekomdkjffnfcjnjam", //to use APIC extension for Chrome
                        "https://salvogame.netlify.com")
                .withSockJS();
    }

}
