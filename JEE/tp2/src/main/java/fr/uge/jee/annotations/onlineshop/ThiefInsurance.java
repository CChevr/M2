package fr.uge.jee.annotations.onlineshop;

import org.springframework.stereotype.Component;

@Component
public class ThiefInsurance implements Insurance {
    public String getDescription() {
        return "Thief insurance";
    }
}
