package org.example.forum.DTO;

import org.example.forum.Entities.Question;
import org.example.forum.Entities.User;

import java.util.List;

public class QuestionDTO {
    private Question question;
    private List<String> tags;
    private User user;

    public QuestionDTO(Question question, List<String> tags, User user) {
        this.question = question;
        this.tags = tags;
        this.user = user;
    }

    public Question getQuestion() {
        return question;
    }

    public List<String> getTags() {
        return tags;
    }

    public User getUser() {
        return user;
    }
}
