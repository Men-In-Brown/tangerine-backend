package com.nighthawk.spring_portfolio.mvc.signup;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class SignUp {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // Other fields for sign-up information

    // Constructors, getters, setters, etc.
}
