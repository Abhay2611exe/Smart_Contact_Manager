package com.smartcontactmanger.services;

import java.util.List;
import java.util.Optional;

import com.smartcontactmanger.entities.User;

public interface UserService {

    User saveUser(User user);
    List<User> getUsers();
    Optional<User> getUserById(String id);
    Optional<User> updateUser(User user);
    void deleteUser(String id);
    boolean isUserExist(String userId);
    boolean isUserExistbyEmail(String email);
    User getUserByEmail(String email);
}
