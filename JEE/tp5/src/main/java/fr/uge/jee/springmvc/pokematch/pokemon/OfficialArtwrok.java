package fr.uge.jee.springmvc.pokematch.pokemon;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OfficialArtwrok {
    @JsonProperty("front_default")
    private String frontDefault;

    public String getFrontDefault() {
        return frontDefault;
    }

    public void setFrontDefault(String frontDefault) {
        this.frontDefault = frontDefault;
    }
}
