package fr.uge.jee.hibernate.students.models;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "Students")
public class Student {
    @Id
    @GeneratedValue
    @Column(name = "studentid")
    private int id;
    // agrégation
    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "Address_Id")
    private Address address;
    // agrégation
    @OneToMany
    @JoinColumn(name = "Student_Id")
    private List<Comment> comments;
    // agrégation
    @ManyToMany
    private Set<Lecture> lectures;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "University_Id")
    private University university;

    public Student() {}

    public Student(Address address, List<Comment> comments, Set<Lecture> lectures, University University) {
        this.address = Objects.requireNonNull(address);
        this.comments = Objects.requireNonNull(comments);
        this.lectures = Objects.requireNonNull(lectures);
        this.university = Objects.requireNonNull(university);
    }

    public int getId() {
        return id;
    }

    public Address getAddress() {
        return address;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public Set<Lecture> getLectures() {
        return lectures;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void setLectures(Set<Lecture> lectures) {
        this.lectures = lectures;
    }
}
