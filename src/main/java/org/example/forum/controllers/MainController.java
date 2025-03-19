package org.example.forum.controllers;

import lombok.AllArgsConstructor;
import org.example.forum.Entities.Question;
import org.example.forum.repositories.QuestionRepository;
import org.example.forum.services.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/main-page")
public class MainController {

    private final QuestionRepository questionRepository;
    private final QuestionService questionService;
    @Autowired
    public MainController(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
        questionService = new QuestionService(questionRepository);
    }

    @GetMapping
    public String mainPage(Model model) {
        List<Question> questionList = questionService.getQuestionsInRange(0, 5);

        List<QuestionDTO> questionDTOs = questionList.stream() // 1
                .map(question -> { // 2
                    // Находим пользователя по userId
                    User user = userRepository.findById(question.getUserId()).orElseThrow(); // 3

                    // Разделяем теги через запятую
                    List<String> tags = List.of(question.getTags().split(",")); // 4

                    // Создаём DTO для передачи в шаблон
                    return new QuestionDTO(question, user, tags); // 5
                })
                .collect(Collectors.toList()); // 6
        model.addAttribute("questionList", questionList);
        return "main-page";
    }

}
