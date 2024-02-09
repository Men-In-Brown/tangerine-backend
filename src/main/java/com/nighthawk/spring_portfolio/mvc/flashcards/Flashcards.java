package com.nighthawk.spring_portfolio.mvc.flashcards;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Flashcards {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String topic;

    @Column(columnDefinition = "TEXT")
    private String question;

    @Column(columnDefinition = "TEXT")
    private String answer;

    public static Flashcards[] init() {
        Flashcards[] flashcardsArray = {
            new Flashcards(null, "Programming", "What is a variable?", "A variable is a container for storing data values."),
            new Flashcards(null, "Math", "What is the value of π (pi)?", "The value of π is approximately 3.14159."),
            new Flashcards(null, "History", "Who was the first President of the United States?", "George Washington"),
        };
        return flashcardsArray;
    }
}
