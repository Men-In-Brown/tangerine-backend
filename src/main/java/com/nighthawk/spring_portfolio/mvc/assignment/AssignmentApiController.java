package com.nighthawk.spring_portfolio.mvc.assignment;

import com.nighthawk.spring_portfolio.mvc.flashcards.Flashcards;
import com.nighthawk.spring_portfolio.mvc.grade.Grade;
import com.nighthawk.spring_portfolio.mvc.grade.GradeJpaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import java.util.Map;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController // annotation to simplify the creation of RESTful web services
@RequestMapping("/api/assignments")  // all requests in file begin with this URI
public class AssignmentApiController {

    // Autowired enables Control to connect URI request and POJO Object to easily for Database CRUD operations
    @Autowired
    private AssignmentJpaRepository repository;

    @Autowired
    private GradeJpaRepository gradeRepository;


    /* GET List of Jokes
     * @GetMapping annotation is used for mapping HTTP GET requests onto specific handler methods.
     */
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/")
    public ResponseEntity<List<Assignment>> getJokes() {
        // ResponseEntity returns List of Jokes provide by JPA findAll()
        return new ResponseEntity<>( repository.findAll(), HttpStatus.OK);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/{id}")
    public ResponseEntity<Assignment> getById(@PathVariable Long id) {
        Optional<Assignment> assignmentOptional = repository.findById(id);
    
        return assignmentOptional.map(assignment -> new ResponseEntity<>(assignment, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/post")
    public ResponseEntity<Object> postPerson(
    @RequestParam("title") String title,
    @RequestParam("desc") String desc,
    @RequestParam("link") String link,
    @RequestParam("maxPoints") int maxPoints) {
        if(title.length() < 3 || title.length() > 100) {
            return new ResponseEntity<>("Title is less than 3 or longer than 100 characters", HttpStatus.BAD_REQUEST);
        }
        if(desc.length() < 3 || desc.length() > 50000) {
            return new ResponseEntity<>("Desc is less than 3 or longer than 500000 characters", HttpStatus.BAD_REQUEST);
        }
        if(link.length() < 3 || link.length() > 400) {
            return new ResponseEntity<>("Link is less than 3 or longer than 400 characters", HttpStatus.BAD_REQUEST);
        }
        if(maxPoints <= 0) {
            return new ResponseEntity<>("maxPoints must be positive", HttpStatus.BAD_REQUEST);
        }
        
        repository.save(new Assignment(title, desc, link, maxPoints));
        return new ResponseEntity<>("Created successfully", HttpStatus.CREATED);
    }
    /* Update Like
     * @PutMapping annotation is used for mapping HTTP PUT requests onto specific handler methods.
     * @PathVariable annotation extracts the templated part {id}, from the URI
     */

    @PostMapping(value = "/submit", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> personStats(@RequestBody final Map<String,Object> request_map) {
        // find ID
        if (!(request_map.get("id") instanceof String)){
            return new ResponseEntity<>("id should be a String", HttpStatus.BAD_REQUEST);
        }
        long id=Long.parseLong((String)request_map.get("id"));  
        Optional<Assignment> optional = repository.findById((id));
        if (optional.isPresent()) {  // Good ID
            Assignment assignment = optional.get();  // value from findByID

            // Extract Attributes from JSON
            Map<String, Object> attributeMap = new HashMap<>();
            for (Map.Entry<String,Object> submission : request_map.entrySet())  {
                // Add needed attributes to attributeMap

                if(submission.getKey().equals("contributors"))
                    if (submission.getValue() instanceof List) {
                        attributeMap.put(submission.getKey(), submission.getValue());
                    } else {
                        return new ResponseEntity<>("Contributors attribute should be a list", HttpStatus.BAD_REQUEST);
                    }
                
                if(submission.getKey().equals("title"))
                    attributeMap.put(submission.getKey(), submission.getValue());
                
                if(submission.getKey().equals("desc"))
                    attributeMap.put(submission.getKey(), submission.getValue());

                if(submission.getKey().equals("link"))
                    attributeMap.put(submission.getKey(), submission.getValue());
                    
            }

            //Does it have all attributes?
            if(!(attributeMap.containsKey("contributors")  && attributeMap.containsKey("title") && attributeMap.containsKey("desc") && attributeMap.containsKey("link"))) {
                return new ResponseEntity<>("Missing attributes. username, contributors, title, desc, and link are required" + attributeMap, HttpStatus.BAD_REQUEST); 
            }

            // Set Date and Attributes to SQL HashMap
            Map<String, Map<String, Object>> date_map = assignment.getSubmissions();
            date_map.put( (String) request_map.get("username"), attributeMap );

            assignment.setSubmissions(date_map);
            repository.save(assignment);

            //Save grade as well for each contributor
            List<String> contributors = (List<String>) attributeMap.get("contributors");
            for (String contributor : contributors)  {
                Grade grade = new Grade(contributor, "temp", assignment.getTitle(), -1);
                gradeRepository.save(grade);
            }



            // return Person with update Stats
            return new ResponseEntity<>(assignment, HttpStatus.OK);
        }
        // return Bad ID
        return new ResponseEntity<>("Bad ID", HttpStatus.BAD_REQUEST); 
    }
}
