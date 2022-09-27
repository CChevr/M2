package fr.uge.jee.onlineshop;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Application {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("config-onlinev3.xml");

        var onlineShop = context.getBean(OnlineShop.class);
        onlineShop.printDescription();
    }
}
