package com.nighthawk.spring_portfolio.mvc.person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class PersonDetailsService implements UserDetailsService {

    @Autowired
    private PersonJpaRepository personJpaRepository;

    @Autowired
    private PersonRoleJpaRepository personRoleJpaRepository;

    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String githubUsername) throws UsernameNotFoundException {
        Person person = personJpaRepository.findByGithubUsername(githubUsername);
        if (person == null) {
            throw new UsernameNotFoundException("User not found with username: " + githubUsername);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        person.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return new org.springframework.security.core.userdetails.User(person.getGithubUsername(), person.getPassword(), authorities);
    }

    public List<Person> listAll() {
        return personJpaRepository.findAllByOrderByNameAsc();
    }

    public List<Person> list(String name, String githubUsername) {
        return personJpaRepository.findByNameContainingIgnoreCaseOrGithubUsernameContainingIgnoreCase(name, githubUsername);
    }

    public List<Person> listLike(String term) {
        return personJpaRepository.findByNameContainingIgnoreCaseOrGithubUsernameContainingIgnoreCase(term, term);
    }

    public List<Person> listLikeNative(String term) {
        String likeTerm = String.format("%%%s%%", term);
        return personJpaRepository.findByLikeTermNative(likeTerm);
    }

    public void save(Person person) {
        person.setPassword(passwordEncoder().encode(person.getPassword()));
        personJpaRepository.save(person);
    }

    public Person get(long id) {
        return personJpaRepository.findById(id).orElse(null);
    }

    public Person getByGithubUsername(String githubUsername) {
        return personJpaRepository.findByGithubUsername(githubUsername);
    }

    public void delete(long id) {
        personJpaRepository.deleteById(id);
    }

    public void defaults(String password, String roleName) {
        for (Person person : listAll()) {
            if (person.getPassword() == null || person.getPassword().isEmpty() || person.getPassword().isBlank()) {
                person.setPassword(passwordEncoder().encode(password));
            }
            if (person.getRoles().isEmpty()) {
                PersonRole role = personRoleJpaRepository.findByName(roleName);
                if (role != null) {
                    person.getRoles().add(role);
                }
            }
        }
    }

    public void saveRole(PersonRole role) {
        PersonRole roleObj = personRoleJpaRepository.findByName(role.getName());
        if (roleObj == null) {
            personRoleJpaRepository.save(role);
        }
    }

    public List<PersonRole> listAllRoles() {
        return personRoleJpaRepository.findAll();
    }

    public PersonRole findRole(String roleName) {
        return personRoleJpaRepository.findByName(roleName);
    }

    public void addRoleToPerson(String githubUsername, String roleName) {
        Person person = personJpaRepository.findByGithubUsername(githubUsername);
        if (person != null) {
            PersonRole role = personRoleJpaRepository.findByName(roleName);
            if (role != null) {
                boolean addRole = person.getRoles().stream().noneMatch(roleObj -> roleObj.getName().equals(roleName));
                if (addRole) person.getRoles().add(role);
            }
        }
    }
}