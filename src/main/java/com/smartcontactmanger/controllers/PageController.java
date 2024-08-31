package com.smartcontactmanger.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.smartcontactmanger.entities.User;
import com.smartcontactmanger.forms.Userform;
import com.smartcontactmanger.helpers.Message;
import com.smartcontactmanger.helpers.MessageType;
import com.smartcontactmanger.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class PageController {

    @Autowired
    private  UserService userService;

    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }

    @RequestMapping("/home")
    public String home() {
        return "home";
    }

    @RequestMapping("/about")
    public String about() {
        return "about";
    }

    @RequestMapping("/service")
    public String service() {
        return "service";
    }

    @RequestMapping("/contact")
    public String contact() {
        return "contact";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String signUp(Model model) {

        Userform userform = new Userform();
       // userform.setName("akashi");
        model.addAttribute("userForm", userform);
        return "register";
    }

    @PostMapping("/do-register")
    public String processRegistrations(@Valid @ModelAttribute("userForm") Userform userForm, BindingResult rBindingResult, HttpSession session) {
        System.out.println("processRegistrations");
      
        //validate the form data
        if(rBindingResult.hasErrors()){
            return "register";
        }

        // form --> user 
        User user = new User();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setPassword(userForm.getPassword());
        user.setAbout(userForm.getAbout());
        user.setEnabled(false);
        user.setProfilPic("https://www.google.com/search?q=defalult+profile+picture&rlz=1C1ONGR_enIN1082IN");

        //save the form data into database
        User savedUser = userService.saveUser(user);

        //message = "registrations successfully"
        Message message = Message.builder().content("Registrations successfully").type(MessageType.green).build();
        session.setAttribute("message", message);           

        //redirect to login page
        return "redirect:/register";
    }

}