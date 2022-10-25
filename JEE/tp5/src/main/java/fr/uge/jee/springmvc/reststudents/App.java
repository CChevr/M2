package fr.uge.jee.springmvc.reststudents;

import fr.uge.jee.springmvc.reststudents.models.Student;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    private void retrieveStudent(long id) {
        WebClient webClient = WebClient.create();
        var monoStudent = webClient.get()
                .uri("http://localhost:8080/students/"+id)
                .retrieve()
                .bodyToMono(Student.class);

        var student = monoStudent.block();
        System.out.println(student);
    }

    private void retrieveStudents() {
        WebClient webClient = WebClient.create();
        var fluxStudent = webClient.get()
                .uri("http://localhost:8080/students/")
                .retrieve()
                .bodyToFlux(Student.class);

        //TODO Passer par les monos du cours
        fluxStudent.toStream().forEach(System.out::println);
    }

    @Bean
    public CommandLineRunner getStudent() {
        //return args -> { retrieveStudent(1L);};
        return args -> { retrieveStudents(); };
    }
}
