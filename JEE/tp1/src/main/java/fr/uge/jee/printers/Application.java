package fr.uge.jee.printers;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Application {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("config-printers.xml");

        var simplePrinter = context.getBean(SimpleMessagePrinter.class);
        var frenchPrinter = context.getBean(FrenchMessagePrinter.class);
        var customizablePrinter = context.getBean(CustomizableMessagePrinter.class);

        simplePrinter.printMessage();
        frenchPrinter.printMessage();
        customizablePrinter.printMessage();


        MessagePrinter printer = context.getBean("CountMessagePrinter",MessagePrinter.class);
        printer.printMessage();
        printer.printMessage();
        printer.printMessage();
        MessagePrinter printer2 =  context.getBean("CountMessagePrinter",MessagePrinter.class);
        printer2.printMessage();


        var slowPrinter = context.getBean("SlowMessagePrinter", MessagePrinter.class);
        slowPrinter.printMessage();
    }
}
