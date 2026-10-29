package com.radhika.tradepilot.service;

import com.radhika.tradepilot.dto.LoginResponse;
import com.radhika.tradepilot.entity.User;
import com.radhika.tradepilot.exception.EmailAlreadyExistsException;
import com.radhika.tradepilot.exception.InvalidCredentialsException;
import com.radhika.tradepilot.jwt.JwtUtil;
import com.radhika.tradepilot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // Register User
    public User registerUser(User user) {

        // Check if email already exists
        User existingUser = userRepository.findByEmail(user.getEmail())
                .orElse(null);

        if (existingUser != null) {
            throw new EmailAlreadyExistsException("Email already exists!");
        }

        // Encrypt Password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Save User
        return userRepository.save(user);
    }

    // Login User
    public LoginResponse loginUser(String email, String password) {

        User user = userRepository.findByEmail(email)
                .orElse(null);

        if (user == null) {
            throw new InvalidCredentialsException("Invalid email or password!");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password!");
        }

        String token = jwtUtil.generateToken(email);

        return new LoginResponse(token);
    }
}