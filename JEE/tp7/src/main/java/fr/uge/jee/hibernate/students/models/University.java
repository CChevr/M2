package fr.uge.jee.hibernate.students.models;

import javax.persistence.*;

@Entity
@Table(name = "Univerties")
public class University {
    @Id
    @GeneratedValue
    @Column(name = "universityid")
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
