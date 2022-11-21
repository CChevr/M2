package fr.uge.jee.hibernate.students.models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue
    @Column(name = "commentid")
    private int id;
    private String comment;

    public Comment() {}

    public Comment(String comment) {
        Objects.requireNonNull(this.comment = comment);
    }

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
