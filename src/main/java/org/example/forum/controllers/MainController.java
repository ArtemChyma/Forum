package org.example.forum.controllers;

import lombok.AllArgsConstructor;
import org.example.forum.DTO.QuestionDTO;
import org.example.forum.Entities.Question;
import org.example.forum.Entities.User;
import org.example.forum.repositories.QuestionRepository;
import org.example.forum.repositories.UserRepository;
import org.example.forum.services.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/main-page")
public class MainController {

    private final QuestionRepository questionRepository;
    private final QuestionService questionService;
    private final UserRepository userRepository;
    @Autowired
    public MainController(QuestionRepository questionRepository, QuestionService questionService, UserRepository userRepository) {
        this.questionRepository = questionRepository;
        this.questionService = questionService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String mainPage(Model model) {
        List<Question> questionList = questionService.getQuestionsInRange(0, 5);

        List<QuestionDTO> questionDTOs = questionList.stream()
                .map(question -> {

                    User user = userRepository.findById(question.getUserId()).orElseThrow();

                    List<String> tags = List.of(question.getTags().split(","));

                    return new QuestionDTO(question, tags, user);
                })
                .toList();
        model.addAttribute("questionList", questionDTOs);
        return "main-page";
    }

}
