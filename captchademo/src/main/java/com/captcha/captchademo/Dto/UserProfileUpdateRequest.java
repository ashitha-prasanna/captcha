package com.captcha.captchademo.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileUpdateRequest {
    private String username;
    private String email;
    private String password;

    // You might also include other fields relevant to your application, such as
    // profile picture, address, etc.

    // Default constructor
    public UserProfileUpdateRequest() {
    }
}
