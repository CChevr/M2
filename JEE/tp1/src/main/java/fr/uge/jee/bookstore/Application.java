package fr.uge.jee.bookstore;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Application {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("config-library.xml");

        var library = context.getBean(Library.class);
        System.out.println(library);
    }
}
