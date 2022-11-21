package fr.uge.jee.hibernate.students.models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Universities")
public class University {
    @Id
    @GeneratedValue
    @Column(name = "universityid")
    private long id;
    private String name;

    public University() {}

    public University(String name) {
        this.name = Objects.requireNonNull(name);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
