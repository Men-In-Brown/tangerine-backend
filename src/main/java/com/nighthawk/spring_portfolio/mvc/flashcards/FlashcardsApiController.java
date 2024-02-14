package com.nighthawk.spring_portfolio.mvc.flashcards;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nighthawk.spring_portfolio.mvc.grade.Grade;

import java.util.List;

@RestController
@RequestMapping("/api/flashcards")
public class FlashcardsApiController {

    @Autowired
    private FlashcardsJpaRepository repository;

    @GetMapping("/")
    public ResponseEntity<List<Flashcards>> getFlashcards() {
        return new ResponseEntity<>(repository.findAll(), HttpStatus.OK);
    }

    @PostMapping( "/add")
    public ResponseEntity<Object> postScore(@RequestParam("topic") String topic,
                                            @RequestParam("question") String question,
                                            @RequestParam("answer") String answer) {
        // A person object WITHOUT ID will create a new record with default roles as student
        Flashcards flashcards = new Flashcards(null, topic, question, answer);
        repository.save(flashcards);
        return new ResponseEntity<>(flashcards +" is created successfully", HttpStatus.CREATED);
    }

    @GetMapping("/topics")
    public ResponseEntity<List<String>> getTopics() {
    List<String> topics = repository.findDistinctTopicBy(); // Fix the method name
    return new ResponseEntity<>(topics, HttpStatus.OK);
    }

    /*@PostMapping("/add")
    public ResponseEntity<Flashcards> addFlashcard(@RequestBody Flashcards flashcard) {
        // Check if the flashcard already exists
        if (repository.existsById(flashcard.getId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        // Save the new flashcard
        Flashcards savedFlashcard = repository.save(flashcard);
        return new ResponseEntity<>(savedFlashcard, HttpStatus.CREATED);
    }*/

    @PutMapping("/update")
    public ResponseEntity<Flashcards> updateFlashcard(@RequestBody Flashcards flashcard) {
        // Check if the flashcard already exists
        if (!repository.existsById(flashcard.getId())) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Update the existing flashcard
        Flashcards updatedFlashcard = repository.save(flashcard);
        return new ResponseEntity<>(updatedFlashcard, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteFlashcard(@PathVariable Long id) {
        // Check if the flashcard exists
        if (!repository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Delete the flashcard
        repository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
