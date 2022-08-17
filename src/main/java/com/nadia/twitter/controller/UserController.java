package com.nadia.twitter.controller;

import com.nadia.twitter.model.User;
import com.nadia.twitter.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public List<User> getAll() {
        return userRepository.getAllUsers();
    }
}