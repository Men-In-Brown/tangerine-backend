package com.nighthawk.spring_portfolio.mvc.person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/mvc/person")
public class PersonViewController {
    @Autowired
    private PersonDetailsService repository;

    @GetMapping("/read")
    public String person(Model model) {
        List<Person> list = repository.listAll();
        model.addAttribute("list", list);
        return "person/read";
    }

    @GetMapping("/create")
    public String personAdd(Person person) {
        return "person/create";
    }

    @PostMapping("/create")
    public String personSave(@Valid Person person, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "person/create";
        }
        repository.save(person);
        repository.addRoleToPerson(person.getGithubUsername(), "ROLE_STUDENT");  // Adapt to GitHub username
        // Redirect to next step
        return "redirect:/mvc/person/read";
    }

    @GetMapping("/update/{id}")
    public String personUpdate(@PathVariable("id") long id, Model model) {
        model.addAttribute("person", repository.get(id));
        return "person/update";
    }

    @PostMapping("/update")
    public String personUpdateSave(@Valid Person person, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "person/update";
        }
        repository.save(person);
        repository.addRoleToPerson(person.getGithubUsername(), "ROLE_STUDENT");  // Adapt to GitHub username
        // Redirect to next step
        return "redirect:/mvc/person/read";
    }

    @GetMapping("/delete/{id}")
    public String personDelete(@PathVariable("id") long id) {
        repository.delete(id);
        return "redirect:/mvc/person/read";
    }

    @GetMapping("/search")
    public String person() {
        return "person/search";
    }
}