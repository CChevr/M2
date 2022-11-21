package fr.uge.jee.hibernate.students.models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "lectures")
public class Lecture {
    @Id
    @GeneratedValue
    @Column(name = "lectureid")
    private long id;
    private String name;

    public Lecture() {}

    public Lecture(String name) {
        this.name = Objects.requireNonNull(name);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
