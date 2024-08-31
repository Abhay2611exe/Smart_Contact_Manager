package com.smartcontactmanger.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.smartcontactmanger.entities.User;
import com.smartcontactmanger.helpers.Helper;
import com.smartcontactmanger.services.UserService;

@ControllerAdvice
public class RootController {

    @Autowired
    private UserService userService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ModelAttribute
    public void addLoggedInUser(Model model, Authentication authentication) {

        if (authentication == null) {
            return;
        }
        String username = Helper.getEmailOfLoggedInUser(authentication);

        logger.info("user name is: {} ", username);

        // get username from database
        User user = userService.getUserByEmail(username);

        logger.info("Username is: {} ", user.getName());
        logger.info("Email is: {} ", user.getEmail());

        model.addAttribute("loggedInUser", user);

    }
}
