package com.nighthawk.spring_portfolio.mvc.person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/person")
public class PersonApiController {

    @Autowired
    private PersonDetailsService personDetailsService;

    @GetMapping("/list")
    public ResponseEntity<List<Person>> listPersons() {
        List<Person> persons = personDetailsService.listAll();
        return ResponseEntity.ok().body(persons);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Person> getPerson(@PathVariable("id") long id) {
        Person person = personDetailsService.get(id);
        return ResponseEntity.ok().body(person);
    }

    @PostMapping("/create")
    public ResponseEntity<Person> createPerson(@RequestBody Person person) {
        personDetailsService.save(person);
        return ResponseEntity.ok().body(person);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Person> updatePerson(@PathVariable("id") long id, @RequestBody Person person) {
        Person existingPerson = personDetailsService.get(id);
        if (existingPerson != null) {
            // Update the fields you want to modify in the existingPerson object
            existingPerson.setName(person.getName());
            existingPerson.setDob(person.getDob());
            // Update other fields as needed

            personDetailsService.save(existingPerson);
            return ResponseEntity.ok().body(existingPerson);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePerson(@PathVariable("id") long id) {
        personDetailsService.delete(id);
        return ResponseEntity.ok().build();
    }
}