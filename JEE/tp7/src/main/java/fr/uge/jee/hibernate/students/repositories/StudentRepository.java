package fr.uge.jee.hibernate.students.repositories;

import fr.uge.jee.hibernate.students.models.*;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
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

    /**
     * Add a lecture to a student
     * @param id Student's id
     * @param lecture lecture to add
     * @return True if it succeeds, false else-way
     */
    public boolean addLecture(long id, Lecture lecture) {
        Objects.requireNonNull(lecture);

        Consumer<EntityManager> consumer = em -> {
            var student = em.find(Student.class, id);
            if (null == student) {
                throw new IllegalArgumentException("Wrong id");
            }
            student.addLectures(lecture);
        };

        try {
            PersistenceUtils.inTransaction(consumer);
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    public Optional<Student> getStudent(long id) {
        Function<EntityManager, Student> function = em -> {
            var student = em.find(Student.class, id);
            if (null == student) {
                throw new IllegalArgumentException("wrong id");
            }
            return student;
        };

        try {
            return Optional.of(PersistenceUtils.inTransaction(function));
        } catch(Exception e) {
            return Optional.empty();
        }
    }

    public Optional<Set<Lecture>> getLectures(long id) {
        Function<EntityManager, Set<Lecture>> function = em -> {
            var query = "SELECT s FROM Student s LEFT JOIN FETCH s.lectures WHERE s.id = :id";
            var student = em.createQuery(query, Student.class)
                    .setParameter("id", id)
                    .getSingleResult();
            if (null == student) {
                throw new IllegalArgumentException("wrong id");
            }
            return student.getLectures();
        };

        try {
            return Optional.of(PersistenceUtils.inTransaction(function));
        } catch(Exception e) {
            return Optional.empty();
        }
    }
}
