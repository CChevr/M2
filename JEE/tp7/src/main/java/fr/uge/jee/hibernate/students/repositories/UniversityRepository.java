package fr.uge.jee.hibernate.students.repositories;

import fr.uge.jee.hibernate.students.models.University;

import javax.persistence.EntityManager;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public class UniversityRepository implements Repository<University, Long> {

    public Class<University> getClazz() {
        return University.class;
    }
}
