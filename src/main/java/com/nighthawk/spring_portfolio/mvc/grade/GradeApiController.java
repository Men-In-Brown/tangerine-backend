package com.nighthawk.spring_portfolio.mvc.grade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
@CrossOrigin(origins = "*", allowedHeaders = "*") // added this line
@RestController
@RequestMapping("/api/grade/")
public class GradeApiController {
    /*
    #### RESTful API ####
    Resource: https://spring.io/guides/gs/rest-service/
    */
    // Autowired enables Control to connect POJO Object through JPA
    @Autowired
    private GradeDetailsService gradeDetailsService; // Add this line

    @Autowired
    private GradeJpaRepository repository;
    /*
    GET List of People
     */
    @GetMapping("/")
    public ResponseEntity<List<Grade>> getScore() {
        return new ResponseEntity<>( repository.findAllByOrderByNameAsc(), HttpStatus.OK);
    }
    /*
    GET individual Car using ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Grade> getScore(@PathVariable long id) {
        Optional<Grade> optional = repository.findById(id);
        if (optional.isPresent()) {  // Good ID
            Grade activity = optional.get();  // value from findByID
            return new ResponseEntity<>(activity, HttpStatus.OK);  // OK HTTP response: status code, headers, and body
        }
        // Bad ID
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    
    // @GetMapping("/searchByEmail")
    // public ResponseEntity<List<Grade>> gradeSearchByEmail(@RequestParam String email) { // Changed ResponseEntity<Object> to ResponseEntity<List<Grade>> since the return type is List<Grade>
    //     List<Grade> list = gradeDetailsService.getByEmail(email); 
    //     return new ResponseEntity<>(list, HttpStatus.OK);
    // }

    @GetMapping("/email/{email}")
    public ResponseEntity<List<Grade>> gradeSearchByEmail(@PathVariable String email) {
        List<Grade> grades = gradeDetailsService.getAllByEmail(email);
        if (!grades.isEmpty()) {
            return new ResponseEntity<>(grades, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    //http://localhost:8087/api/grade/update/3505?newEmail=toby@gmail.com&newAssignment=test&newScore=3.0
    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateScore(@PathVariable long id,
                                            @RequestParam("newEmail") String newEmail,
                                            @RequestParam("newAssignment") String newAssignment,
                                            @RequestParam("newScore") double newScore) {
        Optional<Grade> optional = repository.findById(id);
        if (optional.isPresent()) {
            Grade grade = optional.get(); //read from database
            // Check if the email and assignment match the existing record
            if (grade.getEmail().equals(newEmail) && grade.getAssignment().equals(newAssignment)) {
                // Update the grade
                grade.setScore(newScore);
                repository.save(grade); //send request to update DB
                return new ResponseEntity<>("Grade updated successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Email and assignment do not match existing record", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("Grade not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping( "/post")
    public ResponseEntity<Object> postScore(@RequestParam("email") String email,
                                            @RequestParam("name") String name,
                                            @RequestParam("assignment") String assignment,
                                            @RequestParam("maxPoints") double maxPoints,
                                            @RequestParam("score") double score) {
        // A person object WITHOUT ID will create a new record with default roles as student
        Grade score1 = new Grade(email, name, assignment, maxPoints, score);
        repository.save(score1);
        return new ResponseEntity<>(name +" is created successfully", HttpStatus.CREATED);
    }

    /*
    The personSearch API looks across database for partial match to term (k,v) passed by RequestEntity body
     */
    @PostMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> gradeSearch(@RequestBody final Map<String,String> map) {
        // extract term from RequestEntity
        String term = (String) map.get("term");

        // JPA query to filter on term
        List<Grade> list = repository.findByNameIgnoreCase(term);

        // return resulting list and status, error checking should be added
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}