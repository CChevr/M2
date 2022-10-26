package fr.uge.jee.springmvc.pokematch.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PokeResponse {
    private int count;
    private String next;
    private String previous;

    @JsonProperty("results")
    private List<Pokemon> pokemons;
}
