package fr.uge.jee.hibernate.students.repositories;

import fr.uge.jee.hibernate.students.models.*;

import javax.persistence.EntityManager;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

public class StudentRepository implements Repository<Student, Long> {
    private final UniversityRepository universityRepository = new UniversityRepository();

    public Class<Student> getClazz() { return Student.class; }

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

    /**
     * get all student's lectures
     * @param id Student's id
     * @return All student's lectures
     */
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

    public Optional<University> getUniversity(long id) {
        Function<EntityManager, Student> function = em -> {
            var query = "SELECT s FROM Student s LEFT JOIN FETCH s.university WHERE s.id = :id";
            var response = em.createQuery(query, Student.class)
                    .setParameter("id", id)
                    .getSingleResult();

            if (null == response) {
                throw new IllegalArgumentException("wrong id");
            }
            return response;
        };

        try {
            return Optional.of(PersistenceUtils.inTransaction(function).getUniversity());
        } catch(Exception e) {
            return Optional.empty();
        }

    }

    /**
     * Set student's university
     * @param id id of the student
     * @param university new university
     * @return true in case of successful change, false else-way
     */
    public boolean setUniversity(long id, University university) {
        Objects.requireNonNull(university);

        Consumer<EntityManager> consumer = em -> {
            var query = "SELECT s FROM Student s LEFT JOIN FETCH s.university WHERE s.id = :id";
            var student = em.createQuery(query, Student.class)
                    .setParameter("id", id)
                    .getSingleResult();

            if (null == student) {
                throw new IllegalArgumentException("wrong id");
            }

            student.setUniversity(em.merge(university));
        };

        try {
            PersistenceUtils.inTransaction(consumer);
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    /**
     * Set student's university
     * @param id id of the student
     * @param address new address
     * @return true in case of successful change, false else-way
     */
    public boolean setAddress(long id, Address address) {
        Objects.requireNonNull(address);

        Consumer<EntityManager> consumer = em -> {
            var query = "SELECT s FROM Student s LEFT JOIN FETCH s.address WHERE s.id = :id";
            var student = em.createQuery(query, Student.class)
                    .setParameter("id", id)
                    .getSingleResult();

            if (null == student) {
                throw new IllegalArgumentException("wrong id");
            }

            student.setAddress(em.merge(address));
        };

        try {
            PersistenceUtils.inTransaction(consumer);
            return true;
        } catch(Exception e) {
            return false;
        }
    }
}
