package fr.uge.jee.hibernate.students;

import fr.uge.jee.hibernate.students.models.*;
import fr.uge.jee.hibernate.students.repositories.PersistenceUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import fr.uge.jee.hibernate.students.repositories.LectureRepository;
import fr.uge.jee.hibernate.students.repositories.StudentRepository;
import fr.uge.jee.hibernate.students.repositories.UniversityRepository;

import javax.persistence.EntityManagerFactory;

import static org.junit.jupiter.api.Assertions.*;

public class StudentTests {
    private final EntityManagerFactory emf = PersistenceUtils.getEntityManagerFactory();
    private final LectureRepository lectureRepository = new LectureRepository();
    private final StudentRepository studentRepository = new StudentRepository();
    private final UniversityRepository universityRepository = new UniversityRepository();

    private Address getAddress1() {
        return new Address(11, "Rue de la paix", "Paris");
    }

    private Address getAddress2() {
        return new Address(1, "Avenue de la résistance", "Chelles");
    }

    private Address getAddress3() {
        return new Address(101, "Boulevard Napoleon", "Lyon");
    }

    private Comment getComment1() {
        return new Comment("Commentaire n°1");
    }

    private Comment getComment2() {
        return new Comment("Commentaire n°2");
    }

    private Comment getComment3() {
        return new Comment("Commentaire n°3");
    }

    private Lecture getLecture1() {
        return new Lecture("Mathematics");
    }

    private Lecture getLecture2() {
        return new Lecture("French");
    }

    private Lecture getLecture3() {
        return new Lecture("Music");
    }

    private University getUniversity1() {
        return new University("UGE");
    }

    private University getUniversity2() {
        return new University("UPEM");
    }

    private University getUniversity3() {
        return new University("Université Marne la vallée");
    }

    private Student getStudent1() {
        return new Student(getAddress1(), getUniversity1());
    }

    private Student getStudent2() {
        return new Student(getAddress2(), getUniversity2());
    }

    private Student getStudent3() {
        return new Student(getAddress3(), getUniversity3());
    }

    @BeforeEach
    void dropTables() {
        var em = emf.createEntityManager();
        var tx = em.getTransaction();

        try {
            tx.begin();
            var query = "DELETE FROM STUDENTS_LECTURES CASCADE; " +
                    "DELETE FROM COMMENTS CASCADE; " +
                    "DELETE FROM LECTURES CASCADE; " +
                    "DELETE FROM STUDENTS CASCADE; " +
                    "DELETE FROM ADDRESSES CASCADE; " +
                    "DELETE FROM UNIVERSITIES CASCADE; "
                    ;
            em.createNativeQuery(query).executeUpdate();
            tx.commit();
        } catch(Exception e) {
            tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // STUDENTS
    @Test
    @DisplayName("Empty student DB registration")
    void StudentDBRegistration() {
        var em = emf.createEntityManager();
        var student = new Student();
        assertDoesNotThrow(() -> em.persist(student));
    }

    // UNIVERSITY
    @Test
    @DisplayName("University DB registration")
    void UniversityDBRegistration() {
        var em = emf.createEntityManager();
        assertDoesNotThrow(() -> universityRepository.create(getUniversity1()));

        var countQuerry = "SELECT count(u) FROM University u";
        var count = em.createQuery(countQuerry).getResultList();
        assertEquals(1L, count.get(0));
    }

    @Test
    @DisplayName("University DB Registration without name")
    void UniversityDBRegistrationWithoutName() {
        assertThrows(IllegalArgumentException.class, () -> universityRepository.create(null));

        var em = emf.createEntityManager();
        var countQuerry = "SELECT count(u) FROM University u";
        var count = em.createQuery(countQuerry).getResultList();
        assertEquals(0L, count.get(0));
    }

    @Test
    @DisplayName("University DB deletion")
    void UniversityDBDeletion() {
        var em = emf.createEntityManager();
        var id = universityRepository.create(getUniversity1());

        var countQuerry = "SELECT count(u) FROM University u";
        var count = em.createQuery(countQuerry).getResultList();
        assertEquals(1L, count.get(0));

        assertTrue(() -> universityRepository.delete(id));

        count = em.createQuery(countQuerry).getResultList();
        assertEquals(0L, count.get(0));
    }

    @Test
    @DisplayName("Unexistante University DB Deletion")
    void UnexistanteUniversityDBDeletion() {
        assertFalse(() -> universityRepository.delete(-1L));
    }

    // LECTURE
    @Test
    @DisplayName("Lecture DB registration")
    void LectureDBRegistration() {
        var em = emf.createEntityManager();
        assertDoesNotThrow(() -> lectureRepository.create("Mathematics"));

        var countQuerry = "SELECT count(l) FROM Lecture l";
        var count = em.createQuery(countQuerry).getResultList();
        assertEquals(1L, count.get(0));
    }

    @Test
    @DisplayName("Lecture DB Registration without name")
    void LectureDBRegistrationWithoutName() {
        var em = emf.createEntityManager();
        assertThrows(NullPointerException.class, () -> lectureRepository.create(null));

        var countQuerry = "SELECT count(l) FROM Lecture l";
        var count = em.createQuery(countQuerry).getResultList();
        assertEquals(0L, count.get(0));
    }

    @Test
    @DisplayName("Lecture DB deletion")
    void LectureDBDeletion() {
        var em = emf.createEntityManager();
        var id = lectureRepository.create("Mathematics");

        var countQuerry = "SELECT count(l) FROM Lecture l";
        var count = em.createQuery(countQuerry).getResultList();
        assertEquals(1L, count.get(0));

        assertTrue(() -> lectureRepository.delete(id));

        count = em.createQuery(countQuerry).getResultList();
        assertEquals(0L, count.get(0));
    }

    @Test
    @DisplayName("Unexistante Lecture DB Deletion")
    void UnexistanteLectureDBDeletion() {
        assertFalse(() -> lectureRepository.delete(-1L));
    }

    @Test
    @DisplayName("Add Lecture to studient")
    void AddLectureToStudent() {
        var idLecture = lectureRepository.create(getLecture1().getName());
        var idStudent = studentRepository.create(getStudent1());

        var lecture = lectureRepository.getLecture(idLecture);
        assertTrue(lecture.isPresent());

        studentRepository.addLecture(idStudent, lecture.get());
        var lectures = studentRepository.getLectures(idStudent);

        assertTrue(lectures.isPresent());
        assertEquals(1, lectures.get().size());
    }

    @Test
    @DisplayName("Add lecture to an unknown student")
    void AddLectureToUnknownStudent() {
        var idLecture = lectureRepository.create(getLecture1().getName());

        var lecture = lectureRepository.getLecture(idLecture);
        assertTrue(lecture.isPresent());

        assertFalse(studentRepository.addLecture(-1L, lecture.get()));
    }

    @Test
    @DisplayName("Add unknown lecture to a Student")
    void AddUnknownLectureToStudent() {
        var idStudent = studentRepository.create(getStudent1());

        assertThrows(NullPointerException.class, () -> studentRepository.addLecture(idStudent, null));
    }

    @Test
    @DisplayName("Add unknown lecture to an unknown student")
    void AddUnknownLectureToUnknownStudent() {
        assertThrows(NullPointerException.class, () -> studentRepository.addLecture(-1, null));
    }

    @Test
    @DisplayName("Change Student University")
    void ChangeStudentUniversity() {
        var s = getStudent1();
        var university1 = s.getUniversity();
        var university2 = getUniversity2();
        var idStudent = studentRepository.create(s);
        var student = studentRepository.get(idStudent);

        assertTrue(student.isPresent());
        assertEquals(university1.getName(), student.get().getUniversity().getName());

        var idUniv2 = universityRepository.create(university2);
        assertTrue(studentRepository.setUniversity(idStudent, idUniv2));
        student = studentRepository.get(idStudent);
        assertTrue(student.isPresent());
        assertEquals(university2.getName(), student.get().getUniversity().getName());
    }

    @Test
    @DisplayName("Change Student to unknown university")
    void ChangeStudentUnknownUniversity() {
        var s = getStudent1();
        var university = s.getUniversity();
        var idStudent = studentRepository.create(s);
        var student = studentRepository.get(idStudent);

        assertTrue(student.isPresent());
        assertEquals(university.getName(), student.get().getUniversity().getName());

        assertThrows(NullPointerException.class, () -> studentRepository.setUniversity(idStudent, null));
        student = studentRepository.get(idStudent);
        assertTrue(student.isPresent());
        assertEquals(university.getName(), student.get().getUniversity().getName());
    }

    @Test
    @DisplayName("Change Student's same university")
    void ChangeStudentSameUniversity() {
        var s = getStudent1();
        var university = s.getUniversity();
        var idStudent = studentRepository.create(s);
        var student = studentRepository.get(idStudent);

        assertTrue(student.isPresent());
        assertEquals(university.getName(), student.get().getUniversity().getName());

        assertTrue(studentRepository.setUniversity(idStudent, university.getId()));
        student = studentRepository.get(idStudent);
        assertTrue(student.isPresent());
        assertEquals(university.getName(), student.get().getUniversity().getName());
    }

    @Test
    @DisplayName("Change student university")
    void exchangeStudentUniversity() {
        var s1 = getStudent1();
        var s2 = getStudent2();
        var university1 = s1.getUniversity();
        var university2 = s2.getUniversity();
        var idStudent1 = studentRepository.create(s1);
        var idStudent2 = studentRepository.create(s2);

        var student1 = studentRepository.get(idStudent1);
        var student2 = studentRepository.get(idStudent2);
        assertTrue(student1.isPresent());
        assertTrue(student2.isPresent());
        assertEquals(university1.getName(), student1.get().getUniversity().getName());
        assertEquals(university2.getName(), student2.get().getUniversity().getName());

        assertTrue(studentRepository.setUniversity(idStudent1, university2.getId()));
    }

    @Test
    @DisplayName("Change unknown student university")
    void ChangeUnknownStudentUniversity() {
        var idUniv = universityRepository.create(getUniversity1());
        assertFalse(studentRepository.setUniversity(-1L, idUniv));
    }

    @Test
    @DisplayName("Change Student Address")
    void ChangeStudentAddress()  {
        var s = getStudent1();
        var address1 = s.getAddress();
        var address2 = getAddress2();
        var idStudent = studentRepository.create(s);
        var student = studentRepository.get(idStudent);

        assertTrue(student.isPresent());
        assertEquals(address1.getCity(), student.get().getAddress().getCity());

        assertTrue(studentRepository.setAddress(idStudent, address2));
        student = studentRepository.get(idStudent);
        assertTrue(student.isPresent());
        assertEquals(address2.getCity(), student.get().getAddress().getCity());
    }

    @Test
    @DisplayName("Change student unknown address")
    void changeStudentUnknownAddress() {
        var s = getStudent1();
        var address = s.getAddress();
        var idStudent = studentRepository.create(s);
        var student = studentRepository.get(idStudent);

        assertTrue(student.isPresent());
        assertEquals(address.getCity(), student.get().getAddress().getCity());

        assertThrows(NullPointerException.class, () -> studentRepository.setAddress(idStudent, null));
        student = studentRepository.get(idStudent);
        assertTrue(student.isPresent());
        assertEquals(address.getCity(), student.get().getAddress().getCity());
    }

    @Test
    @DisplayName("Change student to same address")
    void changeStudentSameAddress() {
        var s = getStudent1();
        var address = s.getAddress();
        var idStudent = studentRepository.create(s);
        var student = studentRepository.get(idStudent);

        assertTrue(student.isPresent());
        assertEquals(address.getCity(), student.get().getAddress().getCity());

        assertTrue(studentRepository.setAddress(idStudent, address));
        student = studentRepository.get(idStudent);
        assertTrue(student.isPresent());
        assertEquals(address.getCity(), student.get().getAddress().getCity());
    }
}
