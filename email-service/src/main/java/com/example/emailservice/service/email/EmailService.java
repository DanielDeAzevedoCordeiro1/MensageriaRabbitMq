package com.example.emailservice.service.email;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendRegistrationConfirmationEmail(String to, String name, LocalDateTime createdAt) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Confirmacao de cadastro");
        message.setText(
                "Olá " + name + ",\n\n" +
                        "Seu cadastro foi realizado com sucesso!\n" +
                        "Atenciosamente,\n" +
                        "Equipe de Suporte"
        );

        javaMailSender.send(message);

    }
}
