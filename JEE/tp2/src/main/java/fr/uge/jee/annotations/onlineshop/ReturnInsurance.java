package fr.uge.jee.annotations.onlineshop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ReturnInsurance implements Insurance {
    private final boolean members;

    ReturnInsurance(@Value("false")boolean members) {
        this.members = members;
    }

    public String getDescription() {
        if(members)
            return "Return insurance only for members";
        else
            return "Return insurance";
    }
}
