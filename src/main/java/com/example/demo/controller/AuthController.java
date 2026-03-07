package com.example.demo.controller;

import com.example.demo.dto.RegistrationRequest;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.TokenPairService;
import com.example.demo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final TokenPairService tokenPairService;
    private final UserService userService;

    public AuthController(UserRepository userRepository, PasswordEncoder encoder,
                          TokenPairService tokenPairService, UserService userService) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.tokenPairService = tokenPairService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public Map<String, String> register(@RequestBody RegistrationRequest request) {
        var user = userService.register(request.getUsername(), request.getPassword());
        return tokenPairService.createTokenPair(user);
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Bad credentials"));

        if (!encoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Bad credentials");
        }

        return tokenPairService.createTokenPair(user);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refreshToken");
        if (refreshToken == null || refreshToken.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "refreshToken is required"));
        }
        try {
            return ResponseEntity.ok(tokenPairService.refreshTokenPair(refreshToken));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", e.getMessage()));
        }
    }
}