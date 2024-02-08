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

    @PostMapping( "/post")
    public ResponseEntity<Object> postScore(@RequestParam("name") String name,
                                          @RequestParam("score") float score) {
        // A person object WITHOUT ID will create a new record with default roles as student
        Grade score1 = new Grade(name, score);
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