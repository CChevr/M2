package fr.uge.jee.hibernate.students.models;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "Students")
public class Student implements WithId<Long> {
    @Id
    @GeneratedValue
    @Column(name = "studentid")
    private Long id;
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

    public Student(Address address, University university) {
        this.address = Objects.requireNonNull(address);
        this.university = Objects.requireNonNull(university);
    }

    public Long getId() {
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

    public void setId(long id) {
        this.id = id;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void addLectures(Lecture lecture) {
        this.lectures.add(lecture);
    }

    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        this.university = university;
    }
}