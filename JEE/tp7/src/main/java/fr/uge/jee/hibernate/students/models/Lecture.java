package fr.uge.jee.hibernate.students.models;

import javax.persistence.*;

@Entity
@Table(name = "lectures")
public class Lecture {
    @Id
    @GeneratedValue
    @Column(name = "lectureid")
    private int id;
    private String name;
}
