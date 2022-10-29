package fr.uge.jee.springmvc.pokematch.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.uge.jee.springmvc.pokematch.pokemon.Sprites;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PokeDetails {
    private Sprites sprites;

    public Sprites getSprites() {
        return sprites;
    }

    public void setSprites(Sprites sprites) {
        this.sprites = sprites;
    }

    public String getOfficialFront() {
        return sprites.getOfficialFront();
    }
}
