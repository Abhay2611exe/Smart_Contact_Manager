package com.smartcontactmanger.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smartcontactmanger.entities.Contact;
import com.smartcontactmanger.entities.User;

@Repository
public interface ContactRepo extends JpaRepository<Contact, String> {

    // find all contacts using user
    // custom finder method
    Page<Contact> findByUser(User user, Pageable pageable);

    // custom query method
    @Query("Select c from Contact c where c.user.id = :userId")
    List<Contact> findByUserId(@Param("userId") String userId);

    Page<Contact> findByNameContainingAndUser(String nameKeyword,User user, Pageable pageable);

    Page<Contact> findByEmailContainingAndUser(String emailKeyword,User user, Pageable pageable);

    Page<Contact> findByPhoneNumberContainingAndUser(String phoneNumberKeyword,User user, Pageable pageable);

}
