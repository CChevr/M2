package fr.uge.jee.springmvc.pokematch.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PokemonResponse {
    private int count;
    private String next;
    private String previous;
    @JsonProperty("results")
    private List<Pokemon> pokemons;

    public int getCount() {
        return count;
    }

    public String getNext() {
        return next;
    }

    public String getPrevious() {
        return previous;
    }

    public List<Pokemon> getPokemons() {
        return pokemons;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public void setPokemons(List<Pokemon> pokemons) {
        this.pokemons = pokemons;
    }
}
