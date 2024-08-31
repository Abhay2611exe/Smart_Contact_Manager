package com.smartcontactmanger.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.smartcontactmanger.entities.Contact;
import com.smartcontactmanger.entities.User;

public interface ContactService {

    // Save contact
    Contact save(Contact contact);

    // update contact
    Contact update(Contact contact);

    // Get all the list of contacts
    List<Contact> allContacts();

    // Get contact by Id
    Contact findById(String id);

    // Delete contact by name
    void delete(String id);

    // Get list of contacts by using name, email, and phone
    Page<Contact> searchByName(String nameKeyword, int size, int page, String sortBy, String order, User user);

    Page<Contact> searchByEmail(String emailKeyword, int size, int page, String sortBy, String order, User user);

    Page<Contact> searchByPhoneNumber(String phoneNumberKeyword, int size, int page, String sortBy, String order, User user);

    // Get list of contacts using id
    List<Contact> getByUserId(String userId);

    // Get list of contacts using user
    Page<Contact> getByUser(User user, int page, int pageSize, String sortField, String sortDirection);
}
