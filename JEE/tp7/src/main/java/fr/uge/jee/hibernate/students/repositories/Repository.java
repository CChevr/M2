package fr.uge.jee.hibernate.students.repositories;

import fr.uge.jee.hibernate.students.models.Student;
import fr.uge.jee.hibernate.students.models.WithId;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public interface Repository<T extends WithId<K>, K extends Serializable> {

    Class<T> getClazz();

    default K create(T toBePersisted) {
        return PersistenceUtils.inTransaction(em -> {
            em.persist(toBePersisted);
            return toBePersisted;
        }).getId();
    }

    default boolean delete(K toBeDeletedId) {
        return PersistenceUtils.inTransaction(em -> {
            var toBeDeleted = em.find(getClazz(), toBeDeletedId);
            if (null == toBeDeleted)
                return false;
            em.remove(toBeDeleted);
            return true;
        });
    }

    default T update(T toBeUpdated) {
        return PersistenceUtils.inTransaction(em -> {
            return em.merge(toBeUpdated);
        });
    }

    default Optional<T> get(K id) {
        return PersistenceUtils.inTransaction(em -> {
            return Optional.ofNullable(em.find(getClazz(), id));
        });
    }

    default List<T> getAll() {
        return PersistenceUtils.inTransaction(em -> {
            var query = "SELECT t FROM "+getClazz().getName()+" t";
            return em.createQuery(query, getClazz())
                    .getResultList();
        });
    }
}
