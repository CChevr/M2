package fr.uge.jee.annotations.onlineshop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
public class ClassicShipping implements Delivery {
    @Value("${onlineshop.standarddelivery.delay}")
    private int delay;

    /*
    public ClassicShipping(int delay) {
        this.delay = delay;
    }
     */

    public String getDescription() {
        return "Standard Delivery with a delay of " + delay + " days";
    }
}
