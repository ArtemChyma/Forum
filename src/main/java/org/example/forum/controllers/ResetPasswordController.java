package org.example.forum.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.forum.repositories.UserRepository;
import org.example.forum.services.MailSenderService;
import org.example.forum.validators.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/reset")
public class ResetPasswordController {


    private final UserRepository userRepository;
    private final MailSenderService mailSenderService;

    private final String DEFAULT_LINK_TO_RECOVERY = "http://localhost:8080/reset/";
    private String generateToken() {
        return UUID.randomUUID().toString();
    }
    @Autowired
    public ResetPasswordController(UserRepository userRepository, MailSenderService mailSenderService) {
        this.userRepository = userRepository;
        this.mailSenderService = mailSenderService;
    }

    @GetMapping()
    public String resetPasswordEmail() {
        return "reset-password-stage1";
    }

    @PostMapping()
    public ResponseEntity<Void> sendEmailTo(HttpServletRequest request) {
        String email = request.getParameter("email");
        if (EmailValidator.isValidEmail(email)) {
            mailSenderService.send(email, "Password recovery link", DEFAULT_LINK_TO_RECOVERY + generateToken());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/{token}")
    public String resetPassword(@PathVariable("token") String token) {

        return ;
    }
}
