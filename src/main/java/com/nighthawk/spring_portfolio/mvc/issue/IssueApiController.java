package com.nighthawk.spring_portfolio.mvc.issue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import java.util.Map;

@RestController // annotation to simplify the creation of RESTful web services
@RequestMapping("/api/issues")  // all requests in file begin with this URI
public class IssueApiController {

    // Autowired enables Control to connect URI request and POJO Object to easily for Database CRUD operations
    @Autowired
    private IssueJpaRepository repository;

    /* GET List of Jokes
     * @GetMapping annotation is used for mapping HTTP GET requests onto specific handler methods.
     */
    @GetMapping("/")
    public ResponseEntity<List<Issue>> getJokes() {
        // ResponseEntity returns List of Jokes provide by JPA findAll()
        return new ResponseEntity<>( repository.findAll(), HttpStatus.OK);
    }

    @PostMapping("/post")
    public ResponseEntity<Object> postPerson(
    @RequestParam("title") String title,
    @RequestParam("desc") String desc,
    @RequestParam("username") String username) {
        if(title.length() < 3 || title.length() > 100) {
            return new ResponseEntity<>("Title is less than 3 or longer than 100 characters", HttpStatus.BAD_REQUEST);
        }
        if(desc.length() < 3 || desc.length() > 50000) {
            return new ResponseEntity<>("Desc is less than 3 or longer than 500000 characters", HttpStatus.BAD_REQUEST);
        }
        
        repository.save(new Issue(title, desc, username));
        return new ResponseEntity<>("Created successfully", HttpStatus.CREATED);
    }
    /* Update Like
     * @PutMapping annotation is used for mapping HTTP PUT requests onto specific handler methods.
     * @PathVariable annotation extracts the templated part {id}, from the URI
     */

    @PostMapping(value = "/comment", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> personStats(@RequestBody final Map<String,Object> request_map) {
        // find ID
        if (!(request_map.get("id") instanceof String)){
            return new ResponseEntity<>("id should be a String", HttpStatus.BAD_REQUEST);
        }
        long id=Long.parseLong((String)request_map.get("id"));
        Optional<Issue> optional = repository.findById((id));
        if (optional.isPresent()) {  // Good ID
            Issue assignment = optional.get();  // value from findByID

            // Extract Attributes from JSON
            Map<String, Object> attributeMap = new HashMap<>();
            for (Map.Entry<String,Object> submission : request_map.entrySet())  {
                // Add needed attributes to attributeMap

                if(submission.getKey().equals("username"))
                    attributeMap.put(submission.getKey(), submission.getValue());
                
                if(submission.getKey().equals("desc"))
                    attributeMap.put(submission.getKey(), submission.getValue());

                attributeMap.put("bot","false");
            }

            //Does it have all attributes?
            if(!(attributeMap.containsKey("username") && attributeMap.containsKey("desc"))) {
                return new ResponseEntity<>("Missing attributes. username, desc, and id are required", HttpStatus.BAD_REQUEST); 
            }

            // Set Date and Attributes to SQL HashMap
            Map<String, Map<String, Object>> date_map = assignment.getReplies();
            int numberOfComments = 0;
            if(!assignment.getReplies().isEmpty()) {
                numberOfComments = assignment.getReplies().size();
            }

            date_map.put(Integer.toString(numberOfComments), attributeMap);

            assignment.setReplies(date_map);  // BUG, needs to be customized to replace if existing or append if new

            repository.save(assignment);  // conclude by writing the stats updates

            // return Person with update Stats
            return new ResponseEntity<>(assignment, HttpStatus.OK);
        }
        // return Bad ID
        return new ResponseEntity<>("Bad ID", HttpStatus.BAD_REQUEST); 
    }
}
