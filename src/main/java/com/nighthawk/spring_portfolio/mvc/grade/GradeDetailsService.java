package com.nighthawk.spring_portfolio.mvc.grade;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.core.authority.SimpleGrantedAuthority;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import java.util.ArrayList;
// import java.util.Collection;
import java.util.List;

/*
This class has an instance of Java Persistence API (JPA)
-- @Autowired annotation. Allows Spring to resolve and inject collaborating beans into our bean.
-- Spring Data JPA will generate a proxy instance
-- Below are some CRUD methods that we can use with our database
*/
@Service
@Transactional
public class GradeDetailsService {  // "implements" ties ModelRepo to Spring Security
    // Encapsulate many object into a single Bean (Activities, Roles, and Scrum)
    @Autowired  // Inject ActivitiesJpaRepository
    public GradeJpaRepository gradeJpaRepository;

    /* UserDetailsService Overrides and maps Activities & Roles POJO into Spring Security */

    /* Activities Section */

    public  List<Grade>listAll() {
        return gradeJpaRepository.findAllByOrderByNameAsc();
    }

    // custom query to find match to name or email
    public  List<Grade>list(String name) {
        return gradeJpaRepository.findByNameIgnoreCase(name);
    }

    // custom query to find anything containing term in name or email ignoring case
    public  List<Grade>listLike(String term) {
        return gradeJpaRepository.findByNameIgnoreCase(term);
    }

    // public List<Grade> getByEmail(String email) {
    //     return gradeJpaRepository.findByEmailIgnoreCase(email);
    // }    

    public void save(Grade grade) {
        gradeJpaRepository.save(grade);
    }

    public Grade getGradeById(long id) {
        return (gradeJpaRepository.findById(id).isPresent())
                ? gradeJpaRepository.findById(id).get()
                : null;
    }

    public Grade getByEmail(String email) {
        List<Grade> grades = gradeJpaRepository.findByEmailIgnoreCase(email);
        return !grades.isEmpty() ? grades.get(0) : null;
    }    
    
    public List<Grade> getAllByEmail(String email) {
        return gradeJpaRepository.findByEmailIgnoreCase(email);
    }

    // public Grade getGradeByEmail(String email) {
    //     return (gradeJpaRepository.findByEmailIgnoreCase(email).isPresent())
    //             ? gradeJpaRepository.findByEmailIgnoreCase(email).get()
    //             : null;
    // }

    public Grade getByName(String name) {
        return (gradeJpaRepository.findByName(name));
    }
    
}