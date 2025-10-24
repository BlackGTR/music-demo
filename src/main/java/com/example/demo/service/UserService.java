package com.example.demo.service;

import com.example.demo.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserService {
    private final List<User> users = new ArrayList<>();
    private final AtomicLong nextId = new AtomicLong(1);

    public User addUser(String username) {
        User u = new User();
        u.setId(nextId.getAndIncrement());
        u.setUsername(username);
        users.add(u);
        return u;
    }

    public List<User> getAll() {
        return users;
    }

    public User findById(Long id) {
        return users.stream().filter(u -> u.getId().equals(id)).findFirst().orElse(null);
    }

    public User findByUsername(String username) {
        return users.stream().filter(u -> u.getUsername().equals(username)).findFirst().orElse(null);
    }
}
