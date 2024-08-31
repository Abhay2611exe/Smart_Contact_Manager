package com.smartcontactmanger.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
    
import com.smartcontactmanger.entities.User;
import com.smartcontactmanger.helpers.Message;
import com.smartcontactmanger.helpers.MessageType;
import com.smartcontactmanger.repository.UserRepo;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepo userRepo;

    @GetMapping("/email-verify")
    public String verifyEmail(@RequestParam("token") String token, HttpSession session) {

        // Find user by email token and update their isEmailVerified status to true
        User user = userRepo.findByEmailToken(token).orElse(null);

        if (user != null) {

            if(user.getEmailToken().equals(token)){

                user.setEmailVerified(true);
                user.setEnabled(true);
                userRepo.save(user);
    
                session.setAttribute("message", Message.builder()
                       .content("Email verification successful!")
                       .type(MessageType.green)
                       .build());
                return "success_page";
                 
            }
           
        }

        session.setAttribute("message", Message.builder()
                .content("Something went wrong. Please try again!")
                .type(MessageType.red)
                .build());
        return "error_page.html";
    }
}
