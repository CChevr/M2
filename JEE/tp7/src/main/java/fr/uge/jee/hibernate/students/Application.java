package fr.uge.jee.hibernate.students;

import fr.uge.jee.hibernate.students.repositories.LectureRepository;
import fr.uge.jee.hibernate.students.repositories.UniversityRepository;

import java.util.function.Supplier;

public class Application {
    private static void testCommand(Supplier<Boolean> supplier, String name) {
        if (!supplier.get()) {
            System.out.println("Something gone wrong with "+name);
        } else {
            System.out.println("OK - "+name);
        }
    }

    public static void main(String[] args) {
        // Creation deletion universities
        var universityRepository = new UniversityRepository();
        var upem = universityRepository.create("UPEM");
        var uge = universityRepository.create("Gustave Eiffel");
        testCommand(() -> universityRepository.delete(upem), "upem");

        // Creation deletion lectures
        var lectureRepository = new LectureRepository();
        var francais = lectureRepository.create("Français");
        var math = lectureRepository.create("Mathématiques");
        testCommand(() -> lectureRepository.delete(francais), "français");
    }
}
