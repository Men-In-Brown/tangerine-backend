package com.nighthawk.spring_portfolio.mvc.issue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;

import io.github.cdimascio.dotenv.Dotenv;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import java.util.Map;
import okhttp3.*;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
    @RequestParam("username") String username) throws IOException {
    
        if(title.length() < 3 || title.length() > 100) {
            return new ResponseEntity<>("Title is less than 3 or longer than 100 characters", HttpStatus.BAD_REQUEST);
        }
        if(desc.length() < 3 || desc.length() > 50000) {
            return new ResponseEntity<>("Desc is less than 3 or longer than 500000 characters", HttpStatus.BAD_REQUEST);
        }
        //Get secret api key from .env file
        Dotenv dotenv = Dotenv.load();
        String secretKey = dotenv.get("SECRET");
        
        OkHttpClient client = new OkHttpClient();
        String json = "{\"inputs\":\"<s> [INST] You are MortBot, an experienced code helper. If it fits, you like to say 'Code! Code! Code!' and variations of it by changing Code to another word in certain contexts. Your job is to respond to a issue a student is having, but you will only have one message to respond so avoid asking follow up questions, instead answer to the best of your ability. A student, " + username + " who submitted this issue for you: Title: " + title + ". Description: " + desc + "  [/INST] Model answer</s>\"}";
        okhttp3.RequestBody body = okhttp3.RequestBody.create(
                okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
    
        Request request = new Request.Builder()
                .url("https://api-inference.huggingface.co/models/mistralai/Mixtral-8x7B-Instruct-v0.1")
                .post(body)
                .addHeader("Authorization", secretKey)
                .build();
    
        String responseString;
        try (Response response = client.newCall(request).execute()) {
            responseString = response.body().string();
        }

        
        // Use regex to remove content between <s> and </s>
        String patternString = "<s>.*</s>";
        Pattern pattern = Pattern.compile(patternString, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(responseString);
    
        // If the matcher finds a match
        if (matcher.find()) {
            responseString = matcher.replaceAll("");
        }

        if (responseString.startsWith("[{\"generated_text\":\"")) {
            responseString = responseString.substring(20); // Remove the leading part
        }
        if (responseString.endsWith("\"}")) {
            responseString = responseString.substring(0, responseString.length() - 2); // Remove the trailing part
        }

        repository.save(new Issue(title, desc, username, responseString));

        return new ResponseEntity<>("Created successfully", HttpStatus.CREATED);
    }
    
    /* Update Like
     * @PutMapping annotation is used for mapping HTTP PUT requests onto specific handler methods.
     * @PathVariable annotation extracts the templated part {id}, from the URI
     */

    @PostMapping(value = "/comment", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> comment(@RequestBody final Map<String,Object> request_map) {
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

    @PostMapping(value = "/comment/edit", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> editComment(@RequestBody final Map<String,Object> request_map) {
        // find ID
        if (!(request_map.get("id") instanceof String)){
            return new ResponseEntity<>("id should be a String", HttpStatus.BAD_REQUEST);
        }
        long id=Long.parseLong((String)request_map.get("id"));
        Optional<Issue> optional = repository.findById((id));
        if (optional.isPresent()) {  // Good ID
            Issue assignment = optional.get();  // value from findByID
            if (!(request_map.get("commentId") instanceof String)){
                return new ResponseEntity<>("commentId should be a String", HttpStatus.BAD_REQUEST);
            }
            Map<String, Map<String, Object>> date_map = assignment.getReplies();
            Integer commentId=Integer.parseInt((String)request_map.get("commentId"));
            if (!(date_map.size() >= commentId)) {
                return new ResponseEntity<>("Bad commentId", HttpStatus.BAD_REQUEST);
            }

            // Extract Attributes from JSON
            Map<String, Object> attributeMap = new HashMap<>();
            attributeMap = date_map.get(commentId.toString());
            for (Map.Entry<String,Object> submission : request_map.entrySet())  {
                // Add needed attributes to attributeMap

                if(submission.getKey().equals("username"))
                    attributeMap.put(submission.getKey(), submission.getValue());
                
                if(submission.getKey().equals("desc"))
                    attributeMap.put(submission.getKey(), submission.getValue());

            }


            // Set Date and Attributes to SQL HashMap

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
