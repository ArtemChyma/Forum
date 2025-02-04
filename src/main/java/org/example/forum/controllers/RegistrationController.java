package org.example.forum.controllers;

import org.example.forum.DAO.UserDAOImpl;
import org.example.forum.Entities.User;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    public RegistrationController(UserDAOImpl userDAOImpl) {
        this.userDAOImpl = userDAOImpl;
    }

    @GetMapping
    public String register(Model model, User user) {
        model.addAttribute("user", user);
        return "registration";
    }

    @PostMapping
    public String registerUser(Model model, @ModelAttribute("user") User user) {
        if (userDAOImpl.getUserByEmail(user.getEmail()) != null){
            model.addAttribute("userExists", "User with this email already exists");
        }
        userDAOImpl.saveUser(user);
        return "registration";
    }
}
