package com.smartcontactmanger.controllers;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.smartcontactmanger.entities.Contact;
import com.smartcontactmanger.entities.User;
import com.smartcontactmanger.forms.ContactForm;
import com.smartcontactmanger.forms.ContactSearchForm;
import com.smartcontactmanger.helpers.AppConstants;
import com.smartcontactmanger.helpers.Helper;
import com.smartcontactmanger.helpers.Message;
import com.smartcontactmanger.helpers.MessageType;
import com.smartcontactmanger.services.ContactService;
import com.smartcontactmanger.services.ImageService;
import com.smartcontactmanger.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user/contacts")
public class ContactController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ContactService contactService;

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    // private Logger logger =
    // org.slf4j.LoggerFactory.getLogger(ContactController.class);

    @RequestMapping("/add")
    public String addContactView(Model model) {

        ContactForm contactForm = new ContactForm();
        model.addAttribute("contactForm", contactForm);
        return "/user/add_contact";
    }

    // Create contact Handler
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String saveContact(@Valid @ModelAttribute("contactForm") ContactForm contactForm,
            BindingResult rBindingResult,
            Authentication authentication, HttpSession session) {

        // Validating the contactform data
        if (rBindingResult.hasErrors()) {
            session.setAttribute("message", Message.builder()
                    .content("Correct the following Errors")
                    .type(MessageType.red)
                    .build());
            return "user/add_contact";
        }

        // Getting loggedInUser for Mapping the contact with user
        String userName = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(userName);

        // process the form data
        String filename;
        String fileURL;

        // Image process
        if (contactForm.getContactImage().isEmpty()) {
            // Set the default profile image URL if no image is uploaded
            fileURL = AppConstants.DEFAULT_PROFILE_IMAGE_URL;
            filename = "default-profile"; // or another identifier for the default image
        } else {
            // Upload the user-provided image
            filename = UUID.randomUUID().toString();
            fileURL = imageService.uploadImage(contactForm.getContactImage(), filename);

            if (fileURL == null) {
                session.setAttribute("message", Message.builder()
                        .content("Image upload failed. Please try again.")
                        .type(MessageType.red)
                        .build());
                return "user/add_contact";
            }
        }
        // form --> contact
        Contact contact = new Contact();
        contact.setUser(user);
        contact.setName(contactForm.getName());
        contact.setEmail(contactForm.getEmail());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setAddress(contactForm.getAddress());
        contact.setDescription(contactForm.getDescription());
        contact.setWebsiteLink(contactForm.getWebsiteLink());
        contact.setLinkedInLink(contactForm.getLinkedInLink());
        contact.setFavourite(contactForm.isFavourite());
        contact.setCloudinaryImagePublicId(filename);
        contact.setPicture(fileURL);

        // save contact data to database
        Contact saveContact = contactService.save(contact);
        System.out.println(saveContact);

        // Displaying success message
        session.setAttribute("message", Message.builder()
                .content("Your contact has been saved")
                .type(MessageType.green)
                .build());

        return "redirect:/user/contacts/add";
    }

    // Contact List view Handler
    @RequestMapping
    public String getAllContact(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = AppConstants.PAGE_SIZE + "") int size,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            Model model, Authentication authentication) {

        String username = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(username);
        Page<Contact> pageContacts = contactService.getByUser(user, page, size, sortBy, direction);
        model.addAttribute("pageContacts", pageContacts);
        model.addAttribute("size", AppConstants.PAGE_SIZE);
        model.addAttribute("contactSearchForm", new ContactSearchForm());
        return "/user/contacts";
    }

    // Search Handler
    @RequestMapping("/search")
    public String searchHandler(@ModelAttribute("contactSearchForm") ContactSearchForm contactSearchForm,
            @RequestParam(value = "size", defaultValue = AppConstants.PAGE_SIZE + "") int size,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            Model model,
            Authentication authentication) {

        var user = userService.getUserByEmail(Helper.getEmailOfLoggedInUser(authentication));
        Page<Contact> pageContacts = null;

        if (contactSearchForm.getField().equalsIgnoreCase("name")) {
            pageContacts = contactService.searchByName(contactSearchForm.getValue(), size, page, sortBy, direction,
                    user);
        } else if (contactSearchForm.getField().equalsIgnoreCase("email")) {
            pageContacts = contactService.searchByEmail(contactSearchForm.getValue(), size, page, sortBy, direction,
                    user);
        } else if (contactSearchForm.getField().equalsIgnoreCase("phone")) {
            pageContacts = contactService.searchByPhoneNumber(contactSearchForm.getValue(), size, page, sortBy,
                    direction, user);
        }

        System.out.println("PageContacts: " + pageContacts);

        model.addAttribute("contactSearchForm", contactSearchForm);
        model.addAttribute("pageContacts", pageContacts);
        model.addAttribute("size", AppConstants.PAGE_SIZE);
        logger.info("pageContacts: {}", pageContacts);
        return "/user/search";
    }

    // Delete contact
    @RequestMapping("/delete/{id}")
    public String deleteContact(@PathVariable("id") String id, HttpSession session) {
        contactService.delete(id);
        session.setAttribute("message",
                Message.builder()
                        .content("Contact deleted successfully")
                        .type(MessageType.green)
                        .build());
        return "redirect:/user/contacts";
    }

    // Update contact view handler
    @GetMapping("/view/{id}")
    public String updateContactView(@PathVariable("id") String id, Model model) {

        var contact = contactService.findById(id);

        ContactForm contactForm = new ContactForm();

        contactForm.setName(contact.getName());
        contactForm.setEmail(contact.getEmail());
        contactForm.setPhoneNumber(contact.getPhoneNumber());
        contactForm.setAddress(contact.getAddress());
        contactForm.setDescription(contact.getDescription());
        contactForm.setWebsiteLink(contact.getWebsiteLink());
        contactForm.setLinkedInLink(contact.getLinkedInLink());
        contactForm.setFavourite(contact.isFavourite());
        contactForm.setPicture(contact.getPicture()); // No image to upload
        model.addAttribute("contactForm", contactForm);
        model.addAttribute("contactId", id);

        return "/user/update_contact_view";
    }

    // Update contact handler
    @PostMapping("/update/{id}")
    public String updateContact(@PathVariable("id") String id,
            @Valid @ModelAttribute("contactForm") ContactForm contactForm,
            BindingResult rBindingResult,
            HttpSession session,
            Model model,
            Authentication authentication) {

                if(rBindingResult.hasErrors()){
                    model.addAttribute("contactId", id);
                    model.addAttribute("contactForm", contactForm);
                    return "user/update_contact_view";
                }

                var contact = contactService.findById(id);

                contact.setId(id);
                contact.setName(contactForm.getName());
                contact.setEmail(contactForm.getEmail());
                contact.setPhoneNumber(contactForm.getPhoneNumber());
                contact.setAddress(contactForm.getAddress());
                contact.setDescription(contactForm.getDescription());
                contact.setWebsiteLink(contactForm.getWebsiteLink());
                contact.setLinkedInLink(contactForm.getLinkedInLink());
                contact.setFavourite(contactForm.isFavourite());
                //contact.setPicture(contactForm.getPicture());
                // Check if image is provided
                //load the image
                if(contactForm.getContactImage() != null && !contactForm.getContactImage().isEmpty()){

                    String fileName = UUID.randomUUID().toString();
                    String imageUrl = imageService.uploadImage(contactForm.getContactImage(), fileName);
                    contact.setCloudinaryImagePublicId(fileName);
                    contact.setPicture(imageUrl);
                    contactForm.setPicture(imageUrl);
                }
            
               

                var updatedContact = contactService.update(contact);
                System.out.println(updatedContact);
                session.setAttribute("message",
                        Message.builder()
                               .content("Contact updated successfully")
                               .type(MessageType.green)
                               .build());

        return "redirect:/user/contacts/view/" + id;
    }

}
