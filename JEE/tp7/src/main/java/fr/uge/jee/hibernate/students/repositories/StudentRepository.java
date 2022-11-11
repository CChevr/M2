package fr.uge.jee.hibernate.students.repositories;

import fr.uge.jee.hibernate.students.models.*;

import javax.persistence.EntityManager;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

public class StudentRepository {
    private final UniversityRepository universityRepository = new UniversityRepository();

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

    /**
     * Return the student
     * @param id student's id
     * @return the corresponding student
     */
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

            student.setUniversity(university);
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
            var student = getStudent(id);

            if (student.isEmpty()) {
                throw new IllegalArgumentException("wrong id");
            }

            student.get().setAddress(address);
        };

        try {
            PersistenceUtils.inTransaction(consumer);
            return true;
        } catch(Exception e) {
            return false;
        }
    }
}
