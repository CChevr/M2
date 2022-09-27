package fr.uge.jee.annotations.onlineshop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ClassicShipping implements Delivery {
    private final int delay;

    public ClassicShipping(@Value("999")int delay) {
        this.delay = delay;
    }

    public String getDescription() {
        return "Standard Delivery with a delay of " + delay + " days";
    }
}
