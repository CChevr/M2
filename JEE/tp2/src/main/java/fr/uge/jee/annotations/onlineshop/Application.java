package fr.uge.jee.annotations.onlineshop;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);

        var onlineShop = context.getBean(OnlineShop.class);
        onlineShop.printDescription();
    }
}
