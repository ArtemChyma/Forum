package org.example.forum.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller("/register")
public class RegistrationController {

    @GetMapping
    public String register() {
        return "registration";
    }
}
