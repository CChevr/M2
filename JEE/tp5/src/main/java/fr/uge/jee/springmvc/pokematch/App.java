package fr.uge.jee.springmvc.pokematch;

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
        /*
        SpringApplication.run(App.class, args);
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
        var pokedex = applicationContext.getBean("pokemanager", PokeManager.class);
         */
    }

    // Ne pas utiliser de CommandLineRunner
}
