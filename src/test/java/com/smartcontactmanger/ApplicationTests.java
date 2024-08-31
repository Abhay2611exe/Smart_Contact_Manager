package com.smartcontactmanger;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.smartcontactmanger.services.EmailService;

@SpringBootTest
class ApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private EmailService emailService;

	@Test
    void testSendEmail() {
        emailService.sendEmail("abhay2611.exe@gmail.com",
		 					"This is a testing email address",
							 "This mail is for verification of smartcontact manager.");
    }

}
