package fr.uge.jee.hibernate.students.repositories;

import fr.uge.jee.hibernate.students.models.Lecture;
import fr.uge.jee.hibernate.students.models.Student;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class LectureRepository {

    /**
     * Create a lecture with the given information
     * @param name name of the lecture
     * @return th id of the newly created lecture
     */
    public long create(String name) {
        Objects.requireNonNull(name);

        Function<EntityManager, Lecture> function = em -> {
            var lecture = new Lecture(name);
            em.persist(lecture);
            return lecture;
        };

        return PersistenceUtils.inTransaction(function).getId();
    }

    /**
     * Remove the lecture with the given id from the DB
     * @param id
     * @return true if the removal was successful
     */
    public boolean delete(long id) {
        Consumer<EntityManager> consumer = em -> {
            var lecture = em.find(Lecture.class, id);
            if (null == lecture) {
                throw new IllegalArgumentException("wrong id");
            }
            em.remove(lecture);
        };

        try {
            PersistenceUtils.inTransaction(consumer);
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    public Optional<Lecture> getLecture(long id) {
        Function<EntityManager, Lecture> function = em -> {
            var lecture = em.find(Lecture.class, id);
            if (null == lecture) {
                throw new IllegalArgumentException("wrong id");
            }
            return lecture;
        };

        try {
            return Optional.of(PersistenceUtils.inTransaction(function));
        } catch(Exception e) {
            return Optional.empty();
        }
    }

    /*
    public List<Student> getStudents(long id) {
        Consumer<EntityManager> consumer = em -> {
            var query = "SELECT s FROM Student s LEFT JOIN FETCH s.lecture WHERE s.lecture.id = :id";
            return em.createQuery(query, Student.class)
                    .setParameter("id", id)
                    .getResultList();
        };

        return PersistenceUtils.inTransaction(consumer);
    }
     */
}
