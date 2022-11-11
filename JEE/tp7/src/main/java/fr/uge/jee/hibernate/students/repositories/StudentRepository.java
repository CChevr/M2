package fr.uge.jee.hibernate.students.repositories;

import fr.uge.jee.hibernate.students.models.*;

import javax.persistence.EntityManager;
import java.util.Objects;
import java.util.function.Function;

public class StudentRepository {
    public long create(Address address, University university) {
        Objects.requireNonNull(address);
        Objects.requireNonNull(university);

        Function<EntityManager, Student> function = (EntityManager em) -> {
            var student = new Student(address, university);
            em.persist(student);
            return student;
        };

        return PersistenceUtils.inTransaction(function).getId();
    }
}
