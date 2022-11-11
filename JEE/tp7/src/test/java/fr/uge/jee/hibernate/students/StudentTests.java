package fr.uge.jee.hibernate.students;

import fr.uge.jee.hibernate.students.models.Student;
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

    @BeforeEach
    void dropTables() {
        var em = emf.createEntityManager();
        var tx = em.getTransaction();

        try {
            tx.begin();
            var query = "DELETE FROM UNIVERSITIES CASCADE;" +
                    "DELETE FROM ADDRESSES CASCADE;" +
                    " DELETE FROM COMMENTS CASCADE;" +
                    " DELETE FROM LECTURES CASCADE;" +
                    " DELETE FROM STUDENTS CASCADE;" +
                    " DELETE FROM STUDENTS_LECTURES CASCADE;";
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
        assertDoesNotThrow(() -> universityRepository.create("UGE"));

        var countQuerry = "SELECT count(u) FROM University u";
        var count = em.createQuery(countQuerry).getResultList();
        assertEquals(1L, count.get(0));
    }

    @Test
    @DisplayName("University DB Registration without name")
    void UniversityDBRegistrationWithoutName() {
        var em = emf.createEntityManager();
        assertThrows(NullPointerException.class, () -> universityRepository.create(null));

        var countQuerry = "SELECT count(u) FROM University u";
        var count = em.createQuery(countQuerry).getResultList();
        assertEquals(0L, count.get(0));
    }

    @Test
    @DisplayName("University DB deletion")
    void UniversityDBDeletion() {
        var em = emf.createEntityManager();
        var id = universityRepository.create("UPEM");

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

    // Rajouter un cours à un étudiant
    // Ajouter un cours à un étudiant
    // Ajouter un cours à un étudiant qui n'existe pas
    // Ajouter un cours qui n'existe pas à un étudiant
    // Ajouter un cours qui n'existe pas à un étudiant qui n'existe pas
}
