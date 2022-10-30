package fr.uge.jee.springmvc.pokematch.models;

import org.springframework.stereotype.Component;

import java.util.Objects;

public class Pokemon {
    private final String name;
    private final String image;

    public Pokemon(String name, String image) {
        this.name = Objects.requireNonNull(name);
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getImage() { return image; }
}
