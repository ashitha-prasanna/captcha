package com.captcha.captchademo.Controller;

import com.captcha.captchademo.Dto.AuthenticationResponse;
import com.captcha.captchademo.Dto.UserLoginRequest;
import com.captcha.captchademo.Dto.UserProfileUpdateRequest;
import com.captcha.captchademo.Dto.UserRegistrationRequest;
import com.captcha.captchademo.Service.AuthService;
import com.captcha.captchademo.Service.UserService;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userservice;
    private final AuthService authService;

    @Autowired
    public UserController(UserService userService, AuthService authService){
        this.userservice=userService;
        this.authService=authService;

    }
    @Autowired
    private DefaultKaptcha captchaProducer;


    @PostMapping("/register")
    public ResponseEntity<?>register(@RequestBody UserRegistrationRequest userRegistrationRequest){
        try{
            userservice.registerUser(userRegistrationRequest);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequest loginRequest, HttpServletRequest request) {
        try {
            String sessionCaptcha = (String) request.getSession().getAttribute("captcha");

            if (sessionCaptcha == null || !sessionCaptcha.equalsIgnoreCase(loginRequest.getCaptcha())) {
                return new ResponseEntity<>("Invalid captcha", HttpStatus.FORBIDDEN);
            }
            System.out.println(loginRequest);
            String token = authService.authenticate(loginRequest);
            return ResponseEntity.ok(new AuthenticationResponse(token));
        } catch (Exception e) {
            System.out.println("Exception occured");
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestBody UserProfileUpdateRequest updateRequest) {
        try {
            System.out.println("Updating the user"+updateRequest);
            userservice.updateProfile(updateRequest);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/captcha")
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("image/jpeg");

        String capText = captchaProducer.createText();
        request.getSession().setAttribute("captcha", capText);

        BufferedImage bi = captchaProducer.createImage(capText);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(bi, "jpg", out);

        try {
            out.flush();
        } finally {
            out.close();
        }
    }



}
