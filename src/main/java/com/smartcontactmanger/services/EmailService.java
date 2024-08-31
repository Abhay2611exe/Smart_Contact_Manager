package com.smartcontactmanger.services;

public interface EmailService {

    public void sendEmail(String to, String subject, String body);

    void sendEmailWithHtml();
    
    void sendEmailWithAttachments();
}
