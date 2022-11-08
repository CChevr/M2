package fr.uge.jee.hibernate.employees;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Employees")
public class Employee {
    @Id
    @GeneratedValue
    @Column(name = "EMPLOYEEID")
    private long id;
    private String firstName;
    private String lastName;
    private int salary;

    public Employee() {}

    public Employee(String firstName, String lastName, int salary) {
        this.firstName = Objects.requireNonNull(firstName);
        this.lastName = Objects.requireNonNull(lastName);

        if (salary < 0) {
            throw new IllegalArgumentException("salary must be positive");

        }
        this.salary = salary;
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getSalary() {
        return salary;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return id + " " + firstName + " " + lastName + " " + salary;
    }
}
