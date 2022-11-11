package fr.uge.jee.hibernate.students.repositories;

import fr.uge.jee.hibernate.students.models.Lecture;

import javax.persistence.EntityManager;
import java.util.Objects;
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
}
