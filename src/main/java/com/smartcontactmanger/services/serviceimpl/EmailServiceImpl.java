package com.smartcontactmanger.services.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.smartcontactmanger.services.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Value("${spring.mail.properties.domain_name}")
    private String domain_name;

    @Override
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);

        message.setText(body);
        message.setFrom(domain_name);
        emailSender.send(message);
    }

    @Override
    public void sendEmailWithHtml() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void sendEmailWithAttachments() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
