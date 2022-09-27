package fr.uge.jee.printers;

public class SlowConstructionMessagePrinter implements MessagePrinter {
    SlowConstructionMessagePrinter() throws InterruptedException {
        Thread.sleep(3000);
    }
    @Override
    public void printMessage() {
        System.out.println("Slow Hello World");
    }
}
