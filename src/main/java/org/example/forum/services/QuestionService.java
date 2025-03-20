package org.example.forum.services;

import org.example.forum.Entities.Question;
import org.example.forum.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public List<Question> getQuestionsInRange(int offset, int limit) {
        int page = offset / limit;
        Pageable pageable = PageRequest.of(page, limit, Sort.Direction.DESC, "postTime");
        Page<Question> questionPage = questionRepository.findAll(pageable);
        return questionPage.getContent();
    }

}
