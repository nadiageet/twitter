package com.nadia.twitter.controller;

import com.nadia.twitter.controller.request.CreateUserRequest;
import com.nadia.twitter.model.User;
import com.nadia.twitter.repository.UserJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserJPARepository userRepository;


    @GetMapping("/users")
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @PostMapping("/users")
    public User createUser(@RequestBody CreateUserRequest request) {
        User user = new User();
        user.setUserName(request.getUserName());
        return userRepository.save(user);
    }
}
