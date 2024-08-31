package com.smartcontactmanger.services.serviceimpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.smartcontactmanger.entities.User;
import com.smartcontactmanger.helpers.AppConstants;
import com.smartcontactmanger.helpers.Helper;
import com.smartcontactmanger.helpers.ResourceNotFoundException;
import com.smartcontactmanger.repository.UserRepo;
import com.smartcontactmanger.services.EmailService;
import com.smartcontactmanger.services.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Override
    public User saveUser(User user) {
        // user id : have to generate dynamically
        String userId = UUID.randomUUID().toString();
        user.setUserId(userId);
        // password encoding
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        String emailToken = UUID.randomUUID().toString();
        user.setEmailToken(emailToken);

        // set user role
        user.setRoleList(List.of(AppConstants.ROLE_USER));
        User savedUser = userRepo.save(user);

       

        String emailLink = Helper.getLinkForEmailVerification(emailToken);

        emailService.sendEmail(savedUser.getEmail(), "Verify your Account : Email smart Contact Manager", emailLink);

        return savedUser;
    }

    @Override
    public List<User> getUsers() {
        return userRepo.findAll();
    }

    @Override
    public Optional<User> getUserById(String id) {
        return userRepo.findById(id);
    }

    @Override
    public Optional<User> updateUser(User user) {
        User user2 = userRepo.findById(user.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user2.setName(user.getName());
        user2.setEmail(user.getEmail());
        user2.setPassword(user.getPassword());
        user2.setAbout(user.getAbout());
        user2.setPhoneNumber(user.getPhoneNumber());
        user2.setProfilPic(user.getProfilPic());
        user2.setEnabled(user.isEnabled());
        user2.setEmailVerified(user.isEmailVerified());
        user2.setPhoneVerified(user.isPhoneVerified());
        user2.setProvider(user.getProvider());
        user2.setProviderUserId(user.getProviderUserId());

        User save = userRepo.save(user2);
        return Optional.ofNullable(save);
    }

    @Override
    public void deleteUser(String id) {
        User user = userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userRepo.delete(user);
    }

    @Override
    public boolean isUserExist(String userId) {
        User user = userRepo.findById(userId).orElse(null);
        return user != null;
    }

    @Override
    public boolean isUserExistbyEmail(String email) {
        User user = userRepo.findByEmail(email).orElse(null);
        return user != null;
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email).orElse(null);
    }

}
