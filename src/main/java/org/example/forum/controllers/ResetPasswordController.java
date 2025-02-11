package org.example.forum.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.forum.Entities.Token;
import org.example.forum.Entities.User;
import org.example.forum.repositories.TokenRepository;
import org.example.forum.repositories.UserRepository;
import org.example.forum.services.MailSenderService;
import org.example.forum.validators.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/reset")
public class ResetPasswordController {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final MailSenderService mailSenderService;
    private final String DEFAULT_LINK_TO_RECOVERY = "http://localhost:8080/reset/";
    private final int TIME_TO_EXPIRE = 120000;

    @Autowired
    public ResetPasswordController(UserRepository userRepository, MailSenderService mailSenderService,
                                   TokenRepository tokenRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.mailSenderService = mailSenderService;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
    }
    private String generateToken(String email) {
        String generatedToken = UUID.randomUUID().toString();

        Token token = new Token();
        token.setToken(generatedToken);
        token.setUsed("NOT_USED");
        Optional<User> user = userRepository.findUserByEmail(email);
        user.ifPresent(value -> token.setUserId(value.getId()));

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
    public ResponseEntity<String> sendEmailTo(HttpServletRequest request) {
        String email = request.getParameter("email");
        if (userRepository.findUserByEmail(email).isEmpty()) {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with such email address is not found");
        }
        if (EmailValidator.isValidEmail(email)) {
            mailSenderService.send(email, "Password recovery link", DEFAULT_LINK_TO_RECOVERY + generateToken(email));
        }
        return ResponseEntity.ok().build();
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

    @PostMapping("/{token}")
    public String initiateResetPassword(Model model, @PathVariable("token") String token, HttpServletRequest request) {
        String password = request.getParameter("password");
        String repeatPassword = request.getParameter("repeat-password");
        if (!password.equals(repeatPassword)) {
            model.addAttribute("passwordMatching", "Password is are not identical");
            return "reset-password-stage2";
        }
        Optional<User> user;
        Optional<Token> userToken = tokenRepository.findTokenByToken(token);
        if (userToken.isEmpty()) {
            user = Optional.empty();
        } else {
            user = userRepository.findUserById(userToken.get().getUserId());
        }

        if (user.isEmpty()) {
            throw new RuntimeException("USER WITH SUCH PASSWORD DOES NOT EXIST");
        }
        user.get().setPassword(passwordEncoder.encode(password));
        userRepository.save(user.get());
        return "successful-reset-password";
    }
}
