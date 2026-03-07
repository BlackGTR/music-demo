package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User addUser(String username) {
        User user = new User();
        user.setUsername(username);
        // ДОБАВИЛИ: чтобы сущность проходила @Column(nullable=false)
        user.setPassword(passwordEncoder.encode("123456789"));
        user.setRole("ROLE_USER");

        return userRepository.save(user);
    }
    public User register(String username, String rawPassword) {
        if (username == null || username.isBlank()) {
            throw new RuntimeException("Username is required");
        }
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already taken");
        }
        if (!isStrongPassword(rawPassword)) {
            throw new RuntimeException("Password is too weak: минимум 8 символов, буква, цифра, спецсимвол");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));

        // первый пользователь будет админом
        boolean isFirst = userRepository.count() == 0;
        user.setRole(isFirst ? "ROLE_ADMIN" : "ROLE_USER");

        return userRepository.save(user);
    }
    private boolean isStrongPassword(String password) {
        if (password == null || password.length() < 8) return false;

        boolean hasLetter = false, hasDigit = false, hasSpecial = false;
        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) hasLetter = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else hasSpecial = true;
        }
        return hasLetter && hasDigit && hasSpecial;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    // пример update/delete (CRUD)
    public User updateUsername(Long id, String username) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setUsername(username);
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
