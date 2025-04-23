package com.captcha.captchademo.Service;

import com.captcha.captchademo.Component.JwtTokenProvider;
import com.captcha.captchademo.Dto.UserLoginRequest;
import com.captcha.captchademo.Entity.User;
import com.captcha.captchademo.Exception.UnauthorizedException;
import com.captcha.captchademo.Repository.EventMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final EventMapper eventMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    @Autowired
    public AuthService(EventMapper userMapper, PasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider) {
        this.eventMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }
    public String authenticate(UserLoginRequest loginRequest) {
        // Find the user by email using UserMapper (MyBatis)
        System.out.println("***********************************************" + loginRequest.getEmail());
        User user = eventMapper.findByEmail(loginRequest.getEmail());
        System.out.println("***********************************************" + user);

        // Check if the user exists and if the password matches
        if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            System.out.println("Exception occurred in authenticate");
            throw new UnauthorizedException("Invalid username or password");
        }

        // Generate a token for the user
        String token = tokenProvider.generateToken(user.getUsername());
        return token;
    }



}
