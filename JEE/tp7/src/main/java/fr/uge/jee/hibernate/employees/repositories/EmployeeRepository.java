package fr.uge.jee.hibernate.employees.repositories;

import fr.uge.jee.hibernate.employees.models.Employee;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.swing.text.Utilities;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class EmployeeRepository {
    private final EntityManagerFactory entityManagerFactory = PersistenceUtils.getEntityManagerFactory();

    /**
     * Create an employee with the given information
     * @param firstName
     * @param lastName
     * @param salary
     * @return the id of the newly created employee
     */

    public long create(String firstName, String lastName, int salary) {
        Objects.requireNonNull(firstName);
        Objects.requireNonNull(lastName);

        Function<EntityManager, Employee> function = (EntityManager em) -> {
            var employee = new Employee(firstName, lastName, salary);
            em.persist(employee);
            return employee;
        };

        return PersistenceUtils.inTransaction(function).getId();
    }

    /**
     * Remove the employee with the given id from the DB
     * @param id
     * @return true if the removal was successful
     */

    public boolean delete(long id) {
        Function<EntityManager, Employee> function = (EntityManager em) -> {
            var employee = em.find(Employee.class, id);
            if (null == employee) {
                return null;
            }
            em.remove(employee);
            return employee;
        };

        return (null != PersistenceUtils.inTransaction(function));
    }

    /**
     * Update the salary of the employee with the given id
     * @param id
     * @param salary
     * @return true if the removal was successful
     */

    public boolean update(long id, int salary) {
        Function<EntityManager, Employee> function = (EntityManager em) -> {
            var employee = em.find(Employee.class, id);
            if (null == employee)
                return null;
            employee.setSalary(salary);
            return employee;
        };

        return (null != PersistenceUtils.inTransaction(function));
    }

    /**
     * Retrieve the employee with the given id
     * @param id
     * @return the employee wrapped in an {@link Optional}
     */

    public Optional<Employee> get(long id) {
        Function<EntityManager, Employee> function = (EntityManager em) -> {
            return em.find(Employee.class, id);
        };

        return Optional.ofNullable(PersistenceUtils.inTransaction(function));
    }

    /**
     * Return the list of the employee in the DB
     */

    public List<Employee> getAll() {
        Function<EntityManager, List<Employee>> function = (EntityManager em) -> {
            var q = "SELECT * FROM EMPLOYEE";
            return em.createQuery(q).getResultList();
        };

        return PersistenceUtils.inTransaction(function);
    }
}