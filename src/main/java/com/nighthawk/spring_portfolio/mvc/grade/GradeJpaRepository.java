package com.nighthawk.spring_portfolio.mvc.grade;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/*
Extends the JpaRepository interface from Spring Data JPA.
-- Java Persistent API (JPA) - Hibernate: map, store, update and retrieve database
-- JpaRepository defines standard CRUD methods
-- Via JPA the developer can retrieve database from relational databases to Java objects and vice versa.
 */
public interface GradeJpaRepository extends JpaRepository<Grade, Long> {
    Grade findByName(String name);
    List<Grade> findAllByOrderByNameAsc();
    List<Grade> findByNameIgnoreCase(String name);
    List<Grade> findByEmailIgnoreCase(String email);
    
    @Query(
            value = "SELECT * FROM Grade p WHERE p.name LIKE ?1",
            nativeQuery = true)
    List<Grade> findByLikeTermNative(String term);


}