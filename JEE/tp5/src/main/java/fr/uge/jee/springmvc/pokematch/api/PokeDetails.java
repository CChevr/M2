package fr.uge.jee.springmvc.pokematch.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.uge.jee.springmvc.pokematch.models.Pokemon;

import java.util.Map;
import java.util.Optional;

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

    public Optional<Pokemon> buildPokemon() {
        var sprite = (String) sprites.get("front_default");
        if (sprite != null)
            return Optional.of(new Pokemon(name, sprite));
        return Optional.empty();
    }
}
