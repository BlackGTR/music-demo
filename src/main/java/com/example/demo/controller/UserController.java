package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/add")
    public User addUser(@RequestParam String username) {
        return service.addUser(username);
    }

    @GetMapping
    public List<User> getAll() {
        return service.getAll();
    }
}
