package com.captcha.captchademo.Service;

import com.captcha.captchademo.Dto.UserProfileUpdateRequest;
import com.captcha.captchademo.Dto.UserRegistrationRequest;
import com.captcha.captchademo.Entity.User;
import com.captcha.captchademo.Repository.EventMapper;
import com.captcha.captchademo.Security.AuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private  final EventMapper eventMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationFacade authenticationFacade;

    @Autowired
    public UserService(EventMapper eventMapper, PasswordEncoder passwordEncoder, AuthenticationFacade authenticationFacade){
        this.eventMapper=eventMapper;
        this.passwordEncoder=passwordEncoder;
        this.authenticationFacade=authenticationFacade;
    }
    public void registerUser(UserRegistrationRequest userRegistrationRequest) {
        if(userRegistrationRequest.getPassword().length()<8){
            throw new IllegalArgumentException("Password mush be 8 charecters long");
        }
        if (eventMapper.findByEmail(userRegistrationRequest.getEmail()) != null) {
            throw new IllegalArgumentException("Email already in use");
        }


        User user=new User();
        user.setUsername(userRegistrationRequest.getUsername());
        user.setEmail(userRegistrationRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRegistrationRequest.getPassword()));
        eventMapper.save(user);
    }

    public void updateProfile(UserProfileUpdateRequest updateRequest) {
        System.out.println("authenticationFacade.getCurrentUser() is "+ authenticationFacade.getCurrentUser());
        UserDetails currentUser = authenticationFacade.getCurrentUser();
        String username = currentUser.getUsername(); //or another unique identifier
        User user = eventMapper.findByUsername(username); //assuming you have a method to find by username
        System.out.println("userMapper.findByUsername(username) ->"+eventMapper.findByUsername(username));
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        // Update the user's profile with the new information
        user.setUsername(updateRequest.getUsername());
        user.setEmail(updateRequest.getEmail());
        user.setPassword(passwordEncoder.encode(updateRequest.getPassword()));

        // You might have other fields to update as well
        //You’re setting password directly — it's better to encode it again:
        //user.setPassword(passwordEncoder.encode(updateRequest.getPassword()));

        // Save the updated user back to the database
        eventMapper.save(user);
    }
}
