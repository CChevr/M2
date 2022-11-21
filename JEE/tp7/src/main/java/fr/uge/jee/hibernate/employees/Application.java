package fr.uge.jee.hibernate.employees;

import fr.uge.jee.hibernate.employees.repositories.EmployeeRepository;

public class Application {

    public static void main(String[] args) {
        var employeeRepository = new EmployeeRepository();

        // créé 5 employés dans le DB
        employeeRepository.create("Bob", "Moran", 500);
        employeeRepository.create("Bob", "Dylan", 600);
        employeeRepository.create("Lisa", "Simpson", 100);
        employeeRepository.create("Marge", "Simpson", 1000);
        employeeRepository.create("Homer", "Simpson", 450);

        var employees = employeeRepository.getAll();

        // supprime Lisa Simpsom de BD
        var lisa = employees.stream()
                .filter(e -> e.getFirstName().equals("Lisa"))
                .findFirst();
        lisa.ifPresent(l -> employeeRepository.delete(l.getId()));

        // augmente le salaire de Homer Simpson de 100,
        var homer = employees.stream()
                .filter(e -> e.getFirstName().equals("Homer"))
                .findFirst();
        homer.ifPresent(h -> employeeRepository.update(h.getId()));

        // rajoutez 100 euros à tous les employés qui gagnent moins de 550 euros.
        employeeRepository.getAll().stream()
                .filter(e -> e.getSalary() < 550)
                .forEach(e -> employeeRepository.update(e.getId()));

        // affiche tous les employées dans la base de données
        employeeRepository.getAll().forEach(System.out::println);
    }
}
