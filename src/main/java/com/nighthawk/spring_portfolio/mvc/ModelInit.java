package com.nighthawk.spring_portfolio.mvc;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.nighthawk.spring_portfolio.mvc.assignment.Assignment;
import com.nighthawk.spring_portfolio.mvc.assignment.AssignmentJpaRepository;
import com.nighthawk.spring_portfolio.mvc.grade.Grade;
import com.nighthawk.spring_portfolio.mvc.grade.GradeJpaRepository;
import com.nighthawk.spring_portfolio.mvc.issue.Issue;
import com.nighthawk.spring_portfolio.mvc.issue.IssueJpaRepository;
import com.nighthawk.spring_portfolio.mvc.note.Note;
import com.nighthawk.spring_portfolio.mvc.note.NoteJpaRepository;
import com.nighthawk.spring_portfolio.mvc.person.Person;
import com.nighthawk.spring_portfolio.mvc.person.PersonDetailsService;
import com.nighthawk.spring_portfolio.mvc.person.PersonRole;
import com.nighthawk.spring_portfolio.mvc.person.PersonRoleJpaRepository;

import java.util.List;

@Component
@Configuration // Scans Application for ModelInit Bean, this detects CommandLineRunner
public class ModelInit {  
    @Autowired AssignmentJpaRepository assignmentRepo;
    @Autowired IssueJpaRepository issueRepo;
    @Autowired GradeJpaRepository gradeRepo;
    @Autowired NoteJpaRepository noteRepo;
    @Autowired PersonDetailsService personService;
    @Autowired PersonRoleJpaRepository roleRepo;

    @Bean
    CommandLineRunner run() {  // The run() method will be executed after the application starts
        return args -> {

            // Joke database is populated with starting jokes
            /*String[] jokesArray = Assigment.init();
            for (String joke : jokesArray) {
                List<Assigment> jokeFound = jokesRepo.findByJokeIgnoreCase(joke);  // JPA lookup
                if (jokeFound.size() == 0)
                    jokesRepo.save(new Assigment(null, joke, 0, 0)); //JPA save
            }*/
            // Add assignment data
            assignmentRepo.deleteAll();
            assignmentRepo.save(new Assignment("JQuery Hacks", "Do these JQuery Hacks! Essentially, jQuery is a library that allows us to use some of JavaScript’s built in functions  Benefits of jQuery Benefits of jQuery include:  Makes it easier for us to write JavaScript and HTML code Very flexible in terms of which browsers it can work on Simplifies some of the most common JavaScript functions into fewer lines of code Question: What are some real life applications of jQuery? Name at least two you can think of.  Web pages, used to make dropdown menus appear smoothly Simplifies implementation of AJAX, allows developers to make asynchronous requests to a server and update parts of a web page without requiring a full page reload", "https://nighthawkcoders.github.io/teacher_portfolio//2023/12/07/P1_student_jQuerry_CRUD_IPYNB_2_.html", 2));
            assignmentRepo.save(new Assignment("HashMap Hacks", "Please do these HashMap Hacks! A HashMap store items in “key/value” pairs, and you can access them by an index of another type (e.g. a String). One object is used as a key (index) to another object (value). It can store different types: String keys and Integer values, or the same type, like: String keys and String values.", "https://nighthawkcoders.github.io/teacher_portfolio//2023/12/13/P3_student_HashmapsHashsetsCollections_IPYNB_2_.html", 3));

            //Issue data
            issueRepo.save(new Issue("How to change my port?","I want to change my port on the backend to be on port 8085. How can I change it?", "rachit-j", "Sure thing, Rachit-j! To change the port your backend is running on to 8085, you'll need to modify the configuration in your code. The exact steps will depend on the technology stack you're using, but here's a general idea:\n1. Locate the configuration file where the server port is defined. It could be in a `config` folder, or directly in your `server.js` or `app.js"));

            // adding roles
            PersonRole[] personRoles = PersonRole.init();
            for (PersonRole role : personRoles) {
                PersonRole existingRole = roleRepo.findByName(role.getName());
                if (existingRole != null) {
                    // role already exists
                    continue;
                } else {
                    // role doesn't exist
                    roleRepo.save(role);
                }
            }

            // Person database is populated with test data
            Person[] personArray = Person.init();
            for (Person person : personArray) {
                //findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase
                List<Person> personFound = personService.list(person.getName(), person.getEmail());  // lookup
                if (personFound.size() == 0) {
                    personService.save(person);  // save

                    // Each "test person" starts with a "test note"
                    String text = "Test " + person.getEmail();
                    Note n = new Note(text, person);  // constructor uses new person as Many-to-One association
                    noteRepo.save(n);  // JPA Save
                    personService.addRoleToPerson(person.getEmail(), "ROLE_STUDENT");
                }
            }
            // for lesson demonstration: giving admin role to Mortensen
            personService.addRoleToPerson(personArray[4].getEmail(), "ROLE_ADMIN");

            //delete all entries from grade database
            gradeRepo.deleteAll();
            Grade[] gradeArray = Grade.init();
            for (Grade score : gradeArray) {
                //List<Grade> test = gradeRepo.list(score.getName());  // lookup
                //if (test.size() == 0) {
                    gradeRepo.save(score);
                //};
            }
        };
    }
}

