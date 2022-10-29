package fr.uge.jee.springmvc.pokematch.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.uge.jee.springmvc.pokematch.pokemon.Pokemon;

import java.util.List;

public class PokeResponse implements IPokeResponse {
    private int count;
    private String next;
    private String previous;

    @JsonProperty("results")
    private List<Pokemon> pokemons;

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public String getNext() {
        return next;
    }

    @Override
    public String getPrevious() {
        return previous;
    }

    @Override
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
