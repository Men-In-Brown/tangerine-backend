package com.nighthawk.spring_portfolio.mvc.flashcards;

import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface FlashcardsJpaRepository extends JpaRepository<Flashcards, Long> {

    // Custom methods specific to Flashcards entity
    List<Flashcards> findAllByOrderByQuestionAsc();

    List<Flashcards> findByQuestionIgnoreCase(String question);

    List<Flashcards> findByTopicIgnoreCase(String topic);

    List<String> findDistinctTopicBy(); 
}
