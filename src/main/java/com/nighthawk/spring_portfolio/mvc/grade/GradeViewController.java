package com.nighthawk.spring_portfolio.mvc.grade;

//import org.hibernate.sql.results.LoadingLogger.logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import jakarta.validation.Valid;
import java.util.List;
// Built using article: https://docs.spring.io/spring-framework/docs/3.2.x/spring-framework-reference/html/mvc.html
// or similar: https://asbnotebook.com/2020/04/11/spring-boot-thymeleaf-form-validation-example/


// @CrossOrigin(origins = "http://localhost:4100")
// @RestController
// @RequestMapping("/mvc/grade")

@Controller
@RequestMapping("/mvc/grade")
public class GradeViewController {

    private static final Logger logger = LoggerFactory.getLogger(GradeViewController.class);

    // Autowired enables Control to connect HTML and POJO Object to database easily for CRUD
    @Autowired
    private GradeDetailsService repository;

    @GetMapping("/read")
    public String grade(Model model) {
        List<Grade> list = repository.listAll();
        model.addAttribute("list", list);
        logger.info("Retrieved Grade: {}", list);
        return "grade/read";
    }

    @GetMapping("/read/{id}")
    public String readGrade(@PathVariable Long id, Model model) {
        Grade grade = repository.getGradeById(id);
        if (grade == null) {
            // Handle the case where no grade is found, e.g., redirect or show an error message
            return "error";
        }
        model.addAttribute("grade", grade);
        return "grade/read";
    }

    /*  The HTML template Forms and GradeForm attributes are bound
        @return - template for grade form
        @param - Grade Class
    */
    @GetMapping("/create")
    public String gradeAdd(Grade grade) {
        return "grade/create";
    }

    /* Gathers the attributes filled out in the form, tests for and retrieves validation error
    @param -  object with @Valid
    @param - BindingResult object
     */
    @PostMapping("/create")
    public String gradeSave(@Valid Grade grade, BindingResult bindingResult) {
        // Validation of Decorated GradeForm attributes
        if (bindingResult.hasErrors()) {
            return "grade/create";
        }
        repository.save(grade);
        // Redirect to next step
        return "redirect:/mvc/grade/read";
    }

    @GetMapping("/search")
    public String grade() {
        return "grade/search";
        
    }
    // public ResponseEntity<List<Grade>> searchGradeByName(@RequestParam String name) {
    //     List<Grade> foundGrades = repository.getGradeByName(name);

    //     if (foundGrades.isEmpty()) {
    //         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    //     } else {
    //         return new ResponseEntity<>(foundGrades, HttpStatus.OK);
    //     }
    // }

}