package org.example.forum.controllers;

import lombok.RequiredArgsConstructor;
import org.example.forum.repositories.UserRepository;
import org.example.forum.services.MailSenderService;
import org.example.forum.validators.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/reset")
public class ResetPasswordController {


    private final UserRepository userRepository;
    private final MailSenderService mailSenderService;

    @Autowired
    public ResetPasswordController(UserRepository userRepository, MailSenderService mailSenderService) {
        this.userRepository = userRepository;
        this.mailSenderService = mailSenderService;
    }

    @GetMapping()
    public String resetPasswordEmail() {
        return "reset-password-stage1";
    }

    @PostMapping
    public String sendEmailTo(Model model) {
        String email = (String) model.getAttribute("email");
        if (EmailValidator.isValidEmail(email)) {
            mailSenderService.send(email, "Password recovery link", "");
        }
    }
}
