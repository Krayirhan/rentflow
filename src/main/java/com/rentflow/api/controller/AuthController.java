package com.rentflow.api.controller;

import com.rentflow.api.dto.LoginRequest;
import com.rentflow.api.dto.LoginResponse;
import com.rentflow.model.User;
import com.rentflow.repository.jpa.JpaUserRepository;
import com.rentflow.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Optional<User> user = userRepository.findByUsername(loginRequest.getUsername());

        if (user.isEmpty()) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }

        User foundUser = user.get();

        if (!foundUser.isEnabled()) {
            return ResponseEntity.status(403).body("User account is disabled");
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(), foundUser.getPassword())) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }

        String token = tokenProvider.generateToken(foundUser.getUsername(), foundUser.getRole().getAuthority());

        return ResponseEntity.ok(new LoginResponse(
                token,
                foundUser.getUsername(),
                foundUser.getRole().getAuthority(),
                foundUser.getFullName()
        ));
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);

        if (!tokenProvider.validateToken(token)) {
            return ResponseEntity.status(401).body("Invalid or expired token");
        }

        String username = tokenProvider.getUsernameFromToken(token);
        String role = tokenProvider.getRoleFromToken(token);

        return ResponseEntity.ok()
                .header("X-Username", username)
                .header("X-Role", role)
                .body("Token is valid");
    }
}
