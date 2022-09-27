package fr.uge.jee.printers;

public class CountMessagePrinter implements MessagePrinter {
    private int count = 1;

    @Override
    public void printMessage() {
        System.out.println("This message number "+count);
        count++;
    }
}
