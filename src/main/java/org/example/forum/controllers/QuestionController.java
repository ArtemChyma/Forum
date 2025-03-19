package org.example.forum.controllers;

import org.example.forum.Entities.Question;
import org.example.forum.repositories.QuestionRepository;
import org.example.forum.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.sql.Timestamp;

@Controller
@RequestMapping("/question/")
public class QuestionController {
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    @Value("${app.storage.path}")
    private String FILES_STORAGE_PATH;
    @Autowired
    public QuestionController(UserRepository userRepository, QuestionRepository questionRepository) {
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
    }
    @GetMapping("/ask")
    public String postQuestion(Model model) {
        model.addAttribute("question", new Question());
        return "ask-question";
    }

    @PostMapping("/ask")
    public String processQuestion(Principal principal, @RequestParam("question-images")MultipartFile[] files, @ModelAttribute("question") Question question) {
        if (files != null && files.length != 0) {
            if (userRepository.findUserByEmail(principal.getName()).isPresent()) {
                try {
                    Path uploadPath = Paths.get(FILES_STORAGE_PATH + "users/" + userRepository.findUserByEmail(principal.getName()).get().getId() + "/questions/" + questionRepository.count() + "/images" + "/");
                    if (!Files.exists(uploadPath)) {
                        Files.createDirectories(uploadPath);
                    }
                    for (MultipartFile file : files) {
                        if (!file.isEmpty()) {
                            Path filePath = uploadPath.resolve(file.getOriginalFilename());
                            Files.copy(file.getInputStream(), filePath);
                        }
                    }
                    question.setAnswers(0);
                    question.setViews(0);
                    question.setUserId(userRepository.findUserByEmail(principal.getName()).get().getId());
                    question.setQuestionImages(questionRepository.count() + "/images" + "/");
                    question.setPostTime(new Timestamp(System.currentTimeMillis()));
                    questionRepository.save(question);
                }catch(IOException e){
                    e.printStackTrace();
                    System.out.println("Error multipart file happened");
                }
            }
        }
        return "main-page";
    }

}
