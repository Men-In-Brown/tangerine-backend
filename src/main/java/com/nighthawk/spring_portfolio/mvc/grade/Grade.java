package com.nighthawk.spring_portfolio.mvc.grade;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Data  // Annotations to simplify writing code (ie constructors, setters)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    // email, password, roles are key attributes to login and authentication
    //@Column(unique=true)
    private String name;

    private String email;

    private float score;

    public Grade(String name, float score) {
        this.email = email;
        this.name = name;
        this.score = score;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String newEmail) {
        this.email = newEmail;
    }
    public String getName() {
        return name;
    }
    public void setName(String newName) {
        this.name = newName;
    }
    public float getScore() {
        return score;
    }
    public void setScore(float newscore) {
        this.score = newscore;
    }
 
    public String toString() {
        return "std_grades [id=" + id + ", email=" + email + ", name=" + name + ", score=" + score + "]";
    }

    public static Grade[] init() {
        // basics of class construction
        Grade p1 = new Grade();
        p1.setEmail("toby@gmail.com");
        p1.setName("Thomas Edison");
        p1.setScore(5);
        Grade p2 = new Grade();
        p2.setEmail("lexb@gmail.com");
        p2.setName("Alexander Graham Bell");
        p2.setScore(6);
     
        Grade std_grade[] = {p1, p2};
        return(std_grade);
    }
    public static void main(String[] args) {
        // obtain std_grade from initializer
        Grade std_grade[] = init();
        // iterate using "enhanced for loop"
        for( Grade test : std_grade) {
            System.out.println(test);  // print object
        }
    }
}