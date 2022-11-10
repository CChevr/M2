package fr.uge.jee.hibernate.students.models;

import javax.persistence.*;

@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue
    @Column(name = "commentid")
    private int id;
    private String comment;

    public int getId() {
        return id;
    }

    public String getComment() {
        return comment;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
