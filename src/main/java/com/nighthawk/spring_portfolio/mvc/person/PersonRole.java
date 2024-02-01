package com.nighthawk.spring_portfolio.mvc.person;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

/**
 * Represents roles that can be assigned to a person in the application.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PersonRole {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique=true)
    private String name;

    public PersonRole(String name) {
        this.name = name;
    }

    /**
     * Initializes and returns an array of predefined roles.
     * @return An array of predefined roles.
     */
    public static PersonRole[] init() {
        PersonRole student = new PersonRole("ROLE_STUDENT");
        PersonRole teacher = new PersonRole("ROLE_TEACHER");
        PersonRole admin = new PersonRole("ROLE_ADMIN");
        PersonRole[] initArray = {student, teacher, admin};
        return initArray;
    }
}
