package org.example.forum.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.forum.Entities.Token;
import org.example.forum.repositories.TokenRepository;
import org.example.forum.repositories.UserRepository;
import org.example.forum.services.MailSenderService;
import org.example.forum.validators.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/reset")
public class ResetPasswordController {


    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final MailSenderService mailSenderService;
    private final String DEFAULT_LINK_TO_RECOVERY = "http://localhost:8080/reset/";
    private final int TIME_TO_EXPIRE = 120000;

    @Autowired
    public ResetPasswordController(UserRepository userRepository, MailSenderService mailSenderService,
                                   TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.mailSenderService = mailSenderService;
        this.tokenRepository = tokenRepository;
    }
    private String generateToken() {
        String generatedToken = UUID.randomUUID().toString();

        Token token = new Token();
        token.setToken(generatedToken);
        token.setUsed("NOT_USED");

        Date currentDate = new Date();
        Timestamp creationDate = new Timestamp(currentDate.getTime());
        token.setCreated_at(creationDate);

        Timestamp expirationDate = new Timestamp(currentDate.getTime() + TIME_TO_EXPIRE);
        token.setExpires_at(expirationDate);

        tokenRepository.save(token);
        return generatedToken;
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
    public String resetPassword(Model model, @PathVariable("token") String token) {
        model.addAttribute("token", token);
        Optional<Token> user_token = tokenRepository.findTokenByToken(token);
        if (user_token.isEmpty())
            throw new RuntimeException("TOKEN NOT EXISTS");
        if (user_token.get().getUsed().equals("USED"))
            return "token-used";
        user_token.get().setUsed("USED");
        tokenRepository.save(user_token.get());
        return "reset-password-stage2";
    }
}
