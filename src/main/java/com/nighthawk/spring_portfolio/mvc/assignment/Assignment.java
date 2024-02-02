package com.nighthawk.spring_portfolio.mvc.assignment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.*;

@Data  // Annotations to simplify writing code (ie constructors, setters)
@NoArgsConstructor
@AllArgsConstructor
@Entity // Annotation to simplify creating an entity, which is a lightweight persistence domain object. Typically, an entity represents a table in a relational database, and each entity instance corresponds to a row in that table.
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique=true)
    private String title;

    @Column()
    private String desc;

    @Column()
    private String link;

    /* HashMap is used to store JSON for daily "stats"
    "stats": {
        "2022-11-13": {
            "calories": 2200,
            "steps": 8000
        }
    }
    */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String,Map<String, Object>> submissions = new HashMap<>(); 

    public Assignment(String title, String desc, String link) {
        this.title = title;
        this.desc = desc;
        this.link = link;
    }
}
