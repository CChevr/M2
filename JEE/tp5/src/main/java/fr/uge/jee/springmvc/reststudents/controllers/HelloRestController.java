package fr.uge.jee.springmvc.reststudents.controllers;

import fr.uge.jee.springmvc.reststudents.models.Student;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.Map;

@RestController
public class HelloRestController {
    private final Map<Long, Student> students = Map.of(1L, new Student(1L, "fist", "last"), 2L, new Student(2L, "second", "name"));

    @GetMapping("/students/{id}")
    public Student getStudent(@PathVariable("id") long id) {
        var student = students.get(id);
        if (student==null){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No student with id ("+id+")");
        } else {
            return student;
        }
    }

    @GetMapping("/students")
    public Collection<Student> getStudents() {
        return students.values();
    }
}
