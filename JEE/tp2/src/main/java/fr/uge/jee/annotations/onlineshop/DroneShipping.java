package fr.uge.jee.annotations.onlineshop;

import org.springframework.stereotype.Component;

public class DroneShipping implements Delivery {
    public String getDescription() {
        return "Delivery by Drone";
    }
}
