package com.radhika.tradepilot.controller;
import jakarta.validation.Valid;
import com.radhika.tradepilot.entity.User;
import com.radhika.tradepilot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.radhika.tradepilot.dto.LoginRequest;
import com.radhika.tradepilot.dto.LoginResponse;
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public User registerUser(@Valid @RequestBody User user) {
        return userService.registerUser(user);
    }
    @PostMapping("/login")
    public LoginResponse loginUser(@RequestBody LoginRequest loginRequest)  {

        return userService.loginUser(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );
    }
    @GetMapping("/profile")
    public String profile() {
        return "Welcome to TradePilot Profile";
    }
}