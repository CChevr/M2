package fr.uge.jee.hibernate.employees;

import fr.uge.jee.hibernate.employees.models.Employee;
import fr.uge.jee.hibernate.employees.repositories.PersistenceUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Objects;

public class Application {
    private static void saveEmployee(EntityManager entityManager, Employee employee) {
        Objects.requireNonNull(employee);
        var tx = entityManager.getTransaction();
        try {
            tx.begin();
            entityManager.persist(employee);
            tx.commit();
        } catch(Exception e) {
            tx.rollback();
            throw e;
        } finally {
            entityManager.close();
        }
    }

    public static void main(String[] args) {
        var harry = new Employee("Harry", "Potter", 1000);
        System.out.println(harry);
        //EntityManagerFactory emf = PersistenceUtils.getEntityManagerFactory();
        //saveEmployee(emf.createEntityManager(), harry);
    }
}
