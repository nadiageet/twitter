package com.nadia.twitter.repository;

import com.nadia.twitter.model.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserRepository {

    private final List<User> users = new ArrayList<>();

    public UserRepository() {

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

    public void save(User user) {
        users.add(user);
    }

    public void clear() {
        users.clear();
    }
}
