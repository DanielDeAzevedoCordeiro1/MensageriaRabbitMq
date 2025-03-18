package com.example.emailservice.service.consumer;


import com.example.emailservice.config.RabbitMqConfig;
import com.example.emailservice.dto.UserRegisteredEvent;
import com.example.emailservice.service.email.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Component
public class ConfirmRegisterConsumer {

    private final EmailService emailService;

    public ConfirmRegisterConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = RabbitMqConfig.QUEUE_NAME)
    public void handleUserRegisteredEvent(UserRegisteredEvent userRegisteredEvent) {
        emailService.sendRegistrationConfirmationEmail(
                userRegisteredEvent.email(),
                userRegisteredEvent.name(),
                userRegisteredEvent.createdAt()
        );
    }
}
