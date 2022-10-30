package fr.uge.jee.springmvc.pokematch.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.uge.jee.springmvc.pokematch.models.Pokemon;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PokeDetails {
    private String name;
    private Map<String, Object> sprites;

    public String getName() {
        return name;
    }

    public Map<String, Object> getSprites() {
        return sprites;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSprites(Map<String, Object> sprites) {
        this.sprites = sprites;
    }

    public Pokemon buildPokemon() {
        var sprite = (String) sprites.getOrDefault("front_default", null);
        return new Pokemon(name, sprite);
    }
}
