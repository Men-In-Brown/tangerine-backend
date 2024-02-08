package com.nighthawk.spring_portfolio.mvc.person;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PersonJpaRepository extends JpaRepository<Person, Long> {

    Person findByGithubUsername(String githubUsername);

    List<Person> findAllByOrderByNameAsc();

    List<Person> findByNameContainingIgnoreCaseOrGithubUsernameContainingIgnoreCase(String name, String githubUsername);

    Person findByGithubUsernameAndPassword(String githubUsername, String password);

    @Query(
            value = "SELECT * FROM Person p WHERE p.name LIKE ?1 or p.githubUsername LIKE ?1",
            nativeQuery = true)
    List<Person> findByLikeTermNative(String term);
}