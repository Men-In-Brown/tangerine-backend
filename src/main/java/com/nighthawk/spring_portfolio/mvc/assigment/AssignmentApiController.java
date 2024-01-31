package com.nighthawk.spring_portfolio.mvc.assigment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController // annotation to simplify the creation of RESTful web services
@RequestMapping("/api/assignments")  // all requests in file begin with this URI
public class AssignmentApiController {

    // Autowired enables Control to connect URI request and POJO Object to easily for Database CRUD operations
    @Autowired
    private AssignmentJpaRepository repository;

    /* GET List of Jokes
     * @GetMapping annotation is used for mapping HTTP GET requests onto specific handler methods.
     */
    @GetMapping("/")
    public ResponseEntity<List<Assignment>> getJokes() {
        // ResponseEntity returns List of Jokes provide by JPA findAll()
        return new ResponseEntity<>( repository.findAll(), HttpStatus.OK);
    }

    @PostMapping("/post")
    public ResponseEntity<Object> postPerson(
    @RequestParam("title") String title,
    @RequestParam("desc") String desc,
    @RequestParam("link") String link) {
        if(title.length() < 3 || title.length() > 100) {
            return new ResponseEntity<>("Title is less than 3 or longer than 100 characters", HttpStatus.BAD_REQUEST);
        }
        if(desc.length() < 3 || desc.length() > 50000) {
            return new ResponseEntity<>("Desc is less than 3 or longer than 500000 characters", HttpStatus.BAD_REQUEST);
        }
        if(link.length() < 3 || desc.length() > 100) {
            return new ResponseEntity<>("Link is less than 3 or longer than 100 characters", HttpStatus.BAD_REQUEST);
        }
        
        repository.save(new Assignment(null, title, desc, link));
        return new ResponseEntity<>("Created successfully", HttpStatus.CREATED);
    }
    /* Update Like
     * @PutMapping annotation is used for mapping HTTP PUT requests onto specific handler methods.
     * @PathVariable annotation extracts the templated part {id}, from the URI
     */
}
