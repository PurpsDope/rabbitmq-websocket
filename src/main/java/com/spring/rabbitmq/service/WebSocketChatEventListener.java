package com.spring.rabbitmq.service;

import com.spring.rabbitmq.model.WebSocketChatMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketChatEventListener {

    private final SimpMessageSendingOperations messagingTemplate;

    private final RabbitTemplate rabbitTemplate;

    public WebSocketChatEventListener(SimpMessageSendingOperations messagingTemplate, RabbitTemplate rabbitTemplate) {
        this.messagingTemplate = messagingTemplate;
        this.rabbitTemplate = rabbitTemplate;
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        System.out.println("Received a new web socket connection");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if(username != null) {

            WebSocketChatMessage chatMessage = new WebSocketChatMessage();
            chatMessage.setType(WebSocketChatMessage.MessageType.CHAT);
            chatMessage.setSender(username);

            messagingTemplate.convertAndSend("/topic/javainuse", chatMessage);
            rabbitTemplate.convertAndSend("TestExchange","testRouting", "Test Message");
            // rabbitTemplate.convertAndSend("TestExchange","testRouting","Hello From Code");
        }
    }
}
