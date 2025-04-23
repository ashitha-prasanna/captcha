package com.captcha.captchademo.Security;

import org.springframework.security.core.userdetails.UserDetails;

public interface AuthenticationFacade {
    UserDetails getCurrentUser();
}
