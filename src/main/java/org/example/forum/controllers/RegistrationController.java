package org.example.forum.controllers;

import org.example.forum.DAO.UserDAOImpl;
import org.example.forum.Entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
public class RegistrationController {
    private final UserDAOImpl userDAOImpl;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public RegistrationController(UserDAOImpl userDAOImpl, PasswordEncoder passwordEncoder) {
        this.userDAOImpl = userDAOImpl;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String register(Model model, User user) {
        model.addAttribute("user", user);
        return "registration";
    }

    @PostMapping
    public String registerUser(Model model, @ModelAttribute("user")User user) {
        if (userDAOImpl.getUserByEmail(user.getEmail()).isPresent()){
            model.addAttribute("userExists", "User with this email already exists");
            return "registration";
        }
        user.setRole("ROLE_USER");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDAOImpl.saveUser(user);
        return "redirect:/register";
    }
}
