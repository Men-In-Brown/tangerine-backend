package com.nighthawk.spring_portfolio.mvc.controller;

import com.nighthawk.spring_portfolio.mvc.signup.SignUp;
import com.nighthawk.spring_portfolio.mvc.signup.SignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SignUpController {

    @Autowired
    private SignUpService signUpService;

    @GetMapping("/signup")
    public String showSignUpForm() {
        // Logic to display the sign-up form
        return "signup";
    }

    @PostMapping("/signup")
    public String signUpUser(/* Receive form data as parameters */) {
        // Create a SignUp object with the form data
        SignUp signUp = new SignUp(/* Set form data to SignUp fields */);

        // Call the service to register the user
        signUpService.registerUser(signUp);

        // Redirect to the login page or another appropriate page
        return "redirect:/login";
    }
}
