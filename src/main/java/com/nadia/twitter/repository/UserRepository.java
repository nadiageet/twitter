package com.nadia.twitter.repository;

import com.nadia.twitter.model.User;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class UserRepository {

    private final List<User> users;

    public UserRepository() {
        users = Arrays.asList(
                new User(1L, "nadia"),
                new User(2L, "guigui"),
                new User(3L, "sofia")
        );
    }

    public List<User> getAllUsers() {
        return users;
    }

    public User getUserById(Long id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findAny()
                .orElseThrow(() -> new RuntimeException("user with this id not fond"));
    }
}
