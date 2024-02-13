package com.nighthawk.spring_portfolio.mvc.grade;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // Change strategy to IDENTITY
    private Long id;
    // Other attributes and methods remain unchanged
    // email, password, roles are key attributes to login and authentication
    //@Column(unique=true)
    private String email;
    
    private String name;

    private String assignment;

    private double maxPoints;

    private double score;

    public Grade(String email, String name, String assignment, double maxPoints, double score) {
        this.email = email;
        this.name = name;
        this.assignment = assignment;
        this.maxPoints = maxPoints;
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
    public String getAssignment() {
        return assignment;
    }
    public void setAssignment(String newAssignment) {
        this.assignment = newAssignment;
    }
    public double getMaxPoints() {
        return maxPoints;
    }
    public void setMaxPoints(double newMaxPoints) {
        this.maxPoints = newMaxPoints;
    }
    public double getScore() {
        return score;
    }
    public void setScore(double newscore) {
        this.score = newscore;
    }
 
    public String toString() {
        return "student_grades [id=" + id + ", email=" + email + ", name=" + name + ", assignment=" + assignment + "maxPoints=" + maxPoints + ", score=" + score + "]";
    }

    public static Grade[] init() {
        // basics of class construction
        Grade p1 = new Grade();
        p1.setEmail("toby@gmail.com");
        p1.setName("Thomas Edison");
        p1.setAssignment("JQuery Hacks");
        p1.setMaxPoints(1.00);
        p1.setScore(1.00);
        Grade p2 = new Grade();
        p2.setEmail("lexb@gmail.com");
        p2.setName("Alexander Graham Bell");
        p2.setAssignment("JQuery Hacks");
        p2.setMaxPoints(1.00);
        p2.setScore(0.90);
        Grade p3 = new Grade();
        p3.setEmail("niko@gmail.com");
        p3.setName("Nikola Tesla");
        p3.setAssignment("JQuery Hacks");
        p3.setMaxPoints(1.00);
        p3.setScore(0.55);
        Grade p4 = new Grade();
        p4.setEmail("toby@gmail.com");
        p4.setName("test");
        p4.setAssignment("test");
        p4.setMaxPoints(2.00);
        p4.setScore(2.00);
        Grade student_grade[] = {p1, p2, p3, p4};
        return(student_grade);
    }
    public static void main(String[] args) {
        // obtain student_grade from initializer
        Grade student_grade[] = init();
        // iterate using "enhanced for loop"
        for( Grade test : student_grade) {
            System.out.println(test);  // print object
        }
    }
}