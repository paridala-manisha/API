package com.covidTestManagement.covid.Service;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    // Create a logger
    static final Logger logger = Logger.getLogger(EmailService.class.getName());

    public void sendEmail(SimpleMailMessage message) {
        try {
            javaMailSender.send(message);
            logger.info("Email sent successfully to: " + message.getTo());
        } catch (Exception e) {
            e.printStackTrace();
            logger.severe("Failed to send email: " + e.getMessage());
        }
    }
}
    






