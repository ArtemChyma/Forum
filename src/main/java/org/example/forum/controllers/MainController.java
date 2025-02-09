package org.example.forum.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/main-page")
public class MainController {

    @GetMapping
    public String mainPage() {
        return "main-page";
    }

}
