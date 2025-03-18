package com.example.crudservice.service.producer;


import com.example.crudservice.config.RabbitMqConfig;
import com.example.crudservice.dto.UserRegisteredEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class ConfirmRegisterProducer {

    private final RabbitTemplate rabbitTemplate;

    public ConfirmRegisterProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessageToExchange(UserRegisteredEvent userRegisteredEvent) {
        rabbitTemplate.convertAndSend(
                RabbitMqConfig.EXCHANGE_NAME,
                RabbitMqConfig.ROUTING_KEY,
                userRegisteredEvent
        );
    }
}
