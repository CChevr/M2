package fr.uge.jee.hibernate.students.models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Universities")
public class University implements WithId<Long> {
    @Id
    @GeneratedValue
    @Column(name = "universityid")
    private Long id;
    private String name;

    public University() {}

    public University(String name) {
        this.name = Objects.requireNonNull(name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
