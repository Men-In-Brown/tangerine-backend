package com.nighthawk.spring_portfolio.mvc.assigment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController // annotation to simplify the creation of RESTful web services
@RequestMapping("/api/assignments")  // all requests in file begin with this URI
public class AssigmentApiController {

    // Autowired enables Control to connect URI request and POJO Object to easily for Database CRUD operations
    @Autowired
    private AssigmentJpaRepository repository;

    /* GET List of Jokes
     * @GetMapping annotation is used for mapping HTTP GET requests onto specific handler methods.
     */
    @GetMapping("/")
    public ResponseEntity<List<Assigment>> getJokes() {
        // ResponseEntity returns List of Jokes provide by JPA findAll()
        return new ResponseEntity<>( repository.findAll(), HttpStatus.OK);
    }

    @PostMapping("/post")
    public ResponseEntity<Object> postPerson(@RequestParam("time") long time,
    @RequestParam("title") String title,
    @RequestParam("desc") String desc,
    @RequestParam("list") String list) {

        repository.save(new Assigment(null, title, desc, list));
        return new ResponseEntity<>("Created successfully", HttpStatus.CREATED);
    }
    /* Update Like
     * @PutMapping annotation is used for mapping HTTP PUT requests onto specific handler methods.
     * @PathVariable annotation extracts the templated part {id}, from the URI
     */
}
