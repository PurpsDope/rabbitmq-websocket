package com.spring.rabbitmq.service;

import com.spring.rabbitmq.model.WebSocketChatMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@CrossOrigin
public class ChatController {

    private final RabbitTemplate rabbitTemplate;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(RabbitTemplate rabbitTemplate, SimpMessagingTemplate messagingTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public WebSocketChatMessage handleChat(@Payload WebSocketChatMessage message) {
        rabbitTemplate.convertAndSend("/topic/messages", message);
        return message;
    }

}
