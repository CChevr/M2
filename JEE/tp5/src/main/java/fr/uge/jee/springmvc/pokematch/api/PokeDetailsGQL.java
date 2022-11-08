package fr.uge.jee.springmvc.pokematch.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.uge.jee.springmvc.pokematch.models.Pokemon;

import java.util.*;
import java.util.stream.Collectors;

public class PokeDetailsGQL {
    private String name;
    @JsonProperty("pokemon_v2_pokemonsprites")
    private Sprite[] sprites;

    public static class Sprite {
        private String sprites;
        private Map<String, String> map;

        private static String clearKey(String key) {
            return key.replaceAll("[\":{]", "").replaceAll(" ", ".");
        }

        private static String clearValue(String value) {
            return value.replaceAll("[\"}]", "");
        }

        private void computeMap() {
            map = new HashMap<String, String>();

            Arrays.stream(sprites.split(", "))
                    .filter(s -> !s.contains("null"))
                    .map(str -> str.split(": \\\""))
                    .forEach(x -> map.put(clearKey(x[0]), clearValue(x[1])));
        }

        public String getSprites() {
            return sprites;
        }

        public void setSprites(String sprites) {
            this.sprites = sprites;
        }

        public Optional<String> getSprite(String sprite) {
            if (null == map) {
                computeMap();
            }

            if (map.containsKey(sprite))
                return Optional.of(map.get(sprite));
            return Optional.empty();
        }
    }

    public String getName() {
        return name;
    }

    public Sprite[] getSprites() {
        return sprites;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSprites(Sprite[] sprites) {
        this.sprites = sprites;
    }

    public Optional<Pokemon> buildPokemon() {
        var sprite = sprites[0].getSprite("front_default");
        return sprite.map(s -> new Pokemon(name, s));
    }
}
