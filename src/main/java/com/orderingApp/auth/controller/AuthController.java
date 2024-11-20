package com.orderingApp.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.orderingApp.auth.entity.User;
import com.orderingApp.auth.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;
    
    @Lazy
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        String encodedPassword = (user.getPassword());
        user.setPassword(encodedPassword);
        userService.saveUser(user);

        return ResponseEntity.ok("User registered successfully");
    }
    
    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@RequestBody User user) {
        userService.registerUser(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @SuppressWarnings("deprecation")
	@PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
        User user = userService.findByUsername(username);
        String pass = passwordEncoder.encode(password);
        if (user != null)
        	if(!StringUtils.isEmpty(user.getUsername()) && !StringUtils.isEmpty(user.getPassword()))
        		if(userService.validatePassword(password, user.getPassword()))
        			return ResponseEntity.ok("Login successful for user: " + username);
        return ResponseEntity.ok("Wrong credentials");
    }
    
    @Autowired
    public AuthController(AuthenticationManager authenticationManager) {
    }
}