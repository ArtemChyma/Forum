package org.example.forum.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/question/")
public class QuestionController {

    @GetMapping("/ask")
    public String postQuestion() {
        return "ask-question";
    }

}
