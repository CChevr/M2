package fr.uge.jee.onlineshop;

public class ClassicShipping implements Delivery {
    private final int delay;

    ClassicShipping(int delay) {
        if(delay < 0)
            throw new IllegalArgumentException("Delay must be positive");

        this.delay = delay;
    }

    public String getDescription() {
        return "Standard Delivery with a delay of " + delay + " days";
    }

}
