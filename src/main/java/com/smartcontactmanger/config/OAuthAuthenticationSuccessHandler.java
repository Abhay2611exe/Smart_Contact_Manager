package com.smartcontactmanger.config;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.smartcontactmanger.entities.Providers;
import com.smartcontactmanger.entities.User;
import com.smartcontactmanger.helpers.AppConstants;
import com.smartcontactmanger.repository.UserRepo;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    Logger logger = LoggerFactory.getLogger(OAuthAuthenticationSuccessHandler.class);

    @Autowired
    private UserRepo userRepo;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        logger.info("OAuthAuthenticationSuccessHandler");

        // Identify the provider
        var oauth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        String authorizedClientRegistrationId = oauth2AuthenticationToken.getAuthorizedClientRegistrationId();

        logger.info(authorizedClientRegistrationId);

        var oauthUser = (DefaultOAuth2User) authentication.getPrincipal();

        oauthUser.getAttributes().forEach((key, value) -> {
            logger.info("{} => {}", key, value);
        });

        User user = new User();
        user.setUserId(UUID.randomUUID().toString());
        user.setRoleList(List.of(AppConstants.ROLE_USER));
        user.setEmailVerified(true);
        user.setEnabled(true);
        
        if (authorizedClientRegistrationId.equalsIgnoreCase("google")) {

            // Google configuration
            // TODO: Save data in database and redirect to home page

            user.setEmail(oauthUser.getAttribute("email").toString());
            user.setName(oauthUser.getAttribute("name").toString());
            user.setProfilPic(oauthUser.getAttribute("picture").toString());
            user.setProviderUserId(oauthUser.getName());
            user.setProvider(Providers.GOOGLE);
            user.setPassword("dummy");
            user.setAbout("This account is created by google");
          

        } else if (authorizedClientRegistrationId.equalsIgnoreCase("github")) {

            // Github configuration
            // TODO: Save data in database and redirect to home page

            String email = oauthUser.getAttribute("email") != null ? 
            oauthUser.getAttribute("email").toString() : 
            oauthUser.getAttribute("login").toString() + "@gmail.com";

            String picture = oauthUser.getAttribute("avatar_url").toString();

            String name = oauthUser.getAttribute("name").toString();
            String providerUserId = oauthUser.getName();

            user.setEmail(email);
            user.setName(name);
            user.setProfilPic(picture);
            user.setProviderUserId(providerUserId);
            user.setProvider(Providers.GITHUB);
            user.setPassword("dummy");
            user.setAbout("This accout is created by github");

        } else if (authorizedClientRegistrationId.equalsIgnoreCase("linkedin")) {

            // Linkedin configuration
            // TODO: Save data in database and redirect to home page

        } else {

            logger.info("Unknow provided");

        }

        // Save the user 
        User user1 = userRepo.findByEmail(user.getEmail()).orElse(null);
        if(user1 == null){
            userRepo.save(user);
            logger.info("User created successfully with email: " + user.getEmail());
        }

        /*
         * DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();
         * logger.info(user.getAuthorities().toString());
         * // logger.info(user.getName());
         * 
         * // user.getAttributes().forEach((key, value) -> {
         * // logger.info("{} => {}", key, value);
         * // });
         * 
         * //save data in database
         * 
         * @SuppressWarnings("null")
         * String email = user.getAttribute("email").toString();
         * 
         * @SuppressWarnings("null")
         * String name = user.getAttribute("name").toString();
         * 
         * @SuppressWarnings("null")
         * String picture = user.getAttribute("picture").toString();
         * 
         * // Create a user and save in database
         * User user1 =new User();
         * user1.setEmail(email);
         * user1.setName(name);
         * user1.setProfilPic(picture);
         * user1.setPassword("password");
         * user1.setUserId(UUID.randomUUID().toString());
         * user1.setProvider(Providers.GOOGLE);
         * user1.setEnabled(true);
         * user1.setEmailVerified(true);
         * user1.setProviderUserId(user.getName());
         * user1.setRoleList(List.of(AppConstants.ROLE_USER));
         * user1.setAbout("This account is created by Google");
         * 
         * User user2 = userRepo.findByEmail(email).orElse(null);
         * 
         * if(user2 == null){
         * userRepo.save(user1);
         * logger.info("User created successfully with email: " + email);
         * }
         */


        new DefaultRedirectStrategy().sendRedirect(request, response, "/user/profile");
    }

}
