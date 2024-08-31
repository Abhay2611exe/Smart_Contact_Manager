package com.smartcontactmanger.services.serviceimpl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.smartcontactmanger.entities.Contact;
import com.smartcontactmanger.entities.User;
import com.smartcontactmanger.helpers.ResourceNotFoundException;
import com.smartcontactmanger.repository.ContactRepo;
import com.smartcontactmanger.services.ContactService;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepo contactRepo;

    @Override
    public Contact save(Contact contact) {
      String id = UUID.randomUUID().toString();
      contact.setId(id);
      return contactRepo.save(contact);
    }

    @Override
    public Contact update(Contact contact) {
       
      var oldContact = contactRepo.findById(contact.getId()).orElseThrow(()-> new ResourceNotFoundException("Contact not found"));
       oldContact.setName(contact.getName());
       oldContact.setEmail(contact.getEmail());
       oldContact.setPhoneNumber(contact.getPhoneNumber());
       oldContact.setAddress(contact.getAddress());
       oldContact.setDescription(contact.getDescription());
       oldContact.setWebsiteLink(contact.getWebsiteLink()); 
       oldContact.setLinkedInLink(contact.getLinkedInLink());
       oldContact.setFavourite(contact.isFavourite());
       oldContact.setPicture(contact.getPicture());
       oldContact.setCloudinaryImagePublicId(contact.getCloudinaryImagePublicId());
       //oldContact.setSocialLinks(contact.getSocialLinks());

      return contactRepo.save(oldContact);
    }

    @Override
    public List<Contact> allContacts() {
        List<Contact> contacts = contactRepo.findAll();
        return contacts;
    }

    @Override
    public Contact findById(String id) {
       Contact contact = contactRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Contact not found: " + id));
       return contact;
    }

    @Override
    public void delete(String id) {
       Contact contact = contactRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException());
       contactRepo.delete(contact);
    }

    @Override
    public List<Contact> getByUserId(String userId) {
        return contactRepo.findByUserId(userId);
    }

    @Override
    public Page<Contact> getByUser(User user,int page, int pageSize, String sortBy, String direction) {

        Sort  sort = direction.equals("desc")? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        var pageable = PageRequest.of(page, pageSize);
        return contactRepo.findByUser(user, pageable);
    }

    @Override
    public Page<Contact> searchByName(String nameKeyword, int size, int page, String sortBy, String order, User user) {

        Sort sort = order.equals("desc")? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return contactRepo.findByNameContainingAndUser(nameKeyword,user, pageable);
    
    }

    @Override
    public Page<Contact> searchByEmail(String emailKeyword, int size, int page, String sortBy, String order, User user) {
        Sort sort = order.equals("desc")? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return contactRepo.findByEmailContainingAndUser(emailKeyword,user, pageable);
    }

    @Override
    public Page<Contact> searchByPhoneNumber(String phoneNumberKeyword, int size, int page, String sortBy, String order, User user) {
        Sort sort = order.equals("desc")? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return contactRepo.findByPhoneNumberContainingAndUser(phoneNumberKeyword,user, pageable);
    }



}
