package com.example.backend.controller;

import java.util.Optional;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/signup")
    public User signup(@RequestBody User user) {
        return userRepository.save(user);
    }

    @PostMapping("/login")
    public User login(@RequestBody User loginDetails) {
        Optional<User> user = userRepository.findByEmail(loginDetails.getEmail());
        
        if (user.isPresent() && user.get().getPassword().equals(loginDetails.getPassword())) {
            return user.get();
        } else {
            throw new RuntimeException("Invalid email or password");
        }
    }
}