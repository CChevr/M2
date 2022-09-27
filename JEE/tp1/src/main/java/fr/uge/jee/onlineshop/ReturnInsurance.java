package fr.uge.jee.onlineshop;

public class ReturnInsurance implements Insurance {
    private final boolean members;

    ReturnInsurance(boolean members) {
        this.members = members;
    }

    public String getDescription() {
        if(members)
            return "Return insurance only for members";
        else
            return "Return insurance";
    }
}
