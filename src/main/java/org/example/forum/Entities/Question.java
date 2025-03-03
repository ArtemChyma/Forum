package org.example.forum.Entities;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String questionContent;
    private Timestamp postTime;
    private Long userId;
    private String tags;
    private Integer answers;
    private Integer views;
    private String userImage;
}
