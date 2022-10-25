package fr.uge.jee.springmvc.pokematch.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class User {
    @NotNull(message = "first name can't be empty")
    @Pattern(regexp="^[a-zA-Z]+$",message="Must only contains letters")
    private String firstName;

    @NotNull(message = "last name can't be empty")
    @Pattern(regexp="^[a-zA-Z]+$",message="Must only contains letters")
    private String lastName;

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
