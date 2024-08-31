package com.smartcontactmanger.helpers;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpSession;

@Component
public class SessionHelper {

    @SuppressWarnings("null")
    public static void messageRemove(){
        // Remove message from session
        try{
            HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
            session.removeAttribute("message");
        }catch(Exception e){
            System.out.println("Error removing message from session: "+e.getMessage()); 
        }
      
    }
}
