package com.smartcontactmanger.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smartcontactmanger.entities.User;

@Repository
public interface UserRepo extends JpaRepository<User, String>{

    // Custom finder Methods
    Optional<User> findByEmail(String email);
     Optional<User> findByEmailToken(String id);
    
}
