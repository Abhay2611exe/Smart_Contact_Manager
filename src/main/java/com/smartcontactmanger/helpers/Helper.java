package com.smartcontactmanger.helpers;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class Helper {

    public static String getEmailOfLoggedInUser(Authentication authentication) {

        // AuthenticationPrincipal principal = (AuthenticationPrincipal)
        // authentication.getPrincipal();

        if (authentication instanceof OAuth2AuthenticationToken) {

            var oauth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
            String authorizedClientRegistrationId = oauth2AuthenticationToken.getAuthorizedClientRegistrationId();

            var oauth2User = (OAuth2User) authentication.getPrincipal();
            String username = "";

            // how to fetch the data or username or email when we sign in using google or github
            if (authorizedClientRegistrationId.equalsIgnoreCase("google")) {
                // For Google
                System.out.println("This is from the google account");
                username = oauth2User.getAttribute("email").toString();

            } else if (authorizedClientRegistrationId.equalsIgnoreCase("github")) {
                // For Github
                System.out.println("This is from the github account");
                username = oauth2User.getAttribute("email") != null ? oauth2User.getAttribute("email").toString()
                        : oauth2User.getAttribute("login").toString() + "@gmail.com";
            }

            return username;

        }else{

        }
        // how to fecht the data or username or email when we singup manually
        // Data from the local database
        return authentication.getName();
    }

    public static String getLinkForEmailVerification(String emailToken){
        // Generate a link for email verification
        String link = "http://localhost:8080/auth/verify-email?token="+emailToken;
        return link;
    }
}
