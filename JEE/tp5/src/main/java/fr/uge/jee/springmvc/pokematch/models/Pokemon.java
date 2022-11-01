package fr.uge.jee.springmvc.pokematch.models;

import org.springframework.cache.annotation.Cacheable;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;
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

    /*
    public String getImage() {
        return image;
    }
     */


    @Cacheable(value = "images", key="#pokemon.name")
    public String getImage(Pokemon pokemon) throws IOException {
        System.out.println("Je suis entré ici");
        var url = new URL(this.image);
        InputStream in = new BufferedInputStream(url.openStream());
        return Base64.getEncoder().encodeToString(in.readAllBytes());
    }
}
