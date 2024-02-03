package com.nighthawk.spring_portfolio.mvc.issue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.nighthawk.spring_portfolio.mvc.person.PersonRole;

import jakarta.persistence.*;

import static jakarta.persistence.FetchType.EAGER;

@Data  // Annotations to simplify writing code (ie constructors, setters)
@NoArgsConstructor
@AllArgsConstructor
@Entity // Annotation to simplify creating an entity, which is a lightweight persistence domain object. Typically, an entity represents a table in a relational database, and each entity instance corresponds to a row in that table.
public class Issue {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column()
    private String title;

    @Column()
    private String desc;

    @Column()
    private boolean open;

    @Column()
    private String username;

    @Column()
    private int likes;

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
    private Map<String,Map<String, Object>> replies = new HashMap<>(); 

    public Issue(String title, String desc, String username, String botResponse) {
        this.title = title;
        this.desc = desc;
        this.username = username;
        this.open = true;
        this.likes = 0;

        //Add MortBot comment
        Map<String, Object> innerMap = new HashMap<>();
        innerMap.put("bot", "true");
        innerMap.put("username", "MortBot");
        innerMap.put("desc", botResponse);
        
        this.replies.put("0", innerMap);
    }
}
