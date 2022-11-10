package fr.uge.jee.hibernate.students;

import fr.uge.jee.hibernate.students.models.*;
import fr.uge.jee.hibernate.students.repositories.StudentRepository;

import java.util.ArrayList;
import java.util.HashSet;

public class Application {
    public static void main(String[] args) {
        var address = new Address();
        address.setCity("Champs sur Marne");
        address.setStreetNumber(24);
        address.setStreet("Rue de la paix");

        var university = new University();
        university.setName("Gustave Eiffel");

        var lectures = new HashSet<Lecture>();

        var comments = new ArrayList<Comment>();

        var studentRepository = new StudentRepository();
        var id = studentRepository.create(address, comments, lectures, university);
        //System.out.println("id : "+id);
    }
}
