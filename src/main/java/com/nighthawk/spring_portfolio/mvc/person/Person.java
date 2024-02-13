package com.nighthawk.spring_portfolio.mvc.person;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import com.vladmihalcea.hibernate.type.json.JsonType;
// import org.hibernate.annotations.TypeDef;
// import org.hibernate.annotations.TypeDefs;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
// @TypeDefs({
//     @TypeDef(name = "json", typeClass = JsonType.class),
// })
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty
    @Size(min = 5)
    @Column(unique = true)
    private String githubUsername;

    @NotEmpty
    private String password;

    @NonNull
    @Size(min = 2, max = 30, message = "Name (2 to 30 chars)")
    private String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dob;

    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<PersonRole> roles = new ArrayList<>();

    // @Type(typeClass = JsonType.class)
    @Column(columnDefinition = "jsonb")
    private Map<String, Map<String, Object>> stats = new HashMap<>();

    public Person(String githubUsername, String password, String name, Date dob) {
        this.githubUsername = githubUsername;
        this.password = password;
        this.name = name;
        this.dob = dob;
    }

    public int getAge() {
        if (this.dob != null) {
            LocalDate birthDay = this.dob.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            return Period.between(birthDay, LocalDate.now()).getYears();
        }
        return -1;
    }
    public static Person[] init() {
        Person p1 = new Person();
        p1.setName("Thomas Edison");
        p1.setGithubUsername("tobycoder123");
        p1.setPassword("123Toby!");
        try {
            Date d = new SimpleDateFormat("MM-dd-yyyy").parse("01-01-1840");
            p1.setDob(d);
        } catch (Exception e) {
        }

        Person p2 = new Person();
        p2.setName("Alexander Graham Bell");
        p2.setGithubUsername("lexbcoder123");
        p2.setPassword("123LexB!");
        try {
            Date d = new SimpleDateFormat("MM-dd-yyyy").parse("01-01-1845");
            p2.setDob(d);
        } catch (Exception e) {
        }

        Person p3 = new Person();
        p3.setName("Nikola Tesla");
        p3.setGithubUsername("nikocoder123");
        p3.setPassword("123Niko!");
        try {
            Date d = new SimpleDateFormat("MM-dd-yyyy").parse("01-01-1850");
            p3.setDob(d);
        } catch (Exception e) {
        }

        Person p4 = new Person();
        p4.setName("Madam Currie");
        p4.setGithubUsername("madamcoder123");
        p4.setPassword("123Madam!");
        try {
            Date d = new SimpleDateFormat("MM-dd-yyyy").parse("01-01-1860");
            p4.setDob(d);
        } catch (Exception e) {
        }

        Person p5 = new Person();
        p5.setName("John Mortensen");
        p5.setGithubUsername("jm1021");
        p5.setPassword("123Qwerty!");
        try {
            Date d = new SimpleDateFormat("MM-dd-yyyy").parse("10-21-1959");
            p5.setDob(d);
        } catch (Exception e) {
        }

        Person persons[] = {p1, p2, p3, p4, p5};
        return persons;
    }
    public static void main(String[] args) {
        // obtain Person from initializer
        Person persons[] = init();

        // iterate using "enhanced for loop"
        for( Person person : persons) {
            System.out.println(person);  // print object
        }
    }

}

