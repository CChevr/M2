package fr.uge.jee.hibernate.students.repositories;

import fr.uge.jee.hibernate.students.models.*;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

public class StudentRepository {
    public long create(Address address, List<Comment> comments, Set<Lecture> lectures, University university) {
        Objects.requireNonNull(address);
        Objects.requireNonNull(comments);
        Objects.requireNonNull(lectures);
        Objects.requireNonNull(university);

        Function<EntityManager, Student> function = (EntityManager em) -> {
            var student = new Student(address, comments, lectures, university);
            em.persist(student);
            return student;
        };

        return PersistenceUtils.inTransaction(function).getId();
    }
}
