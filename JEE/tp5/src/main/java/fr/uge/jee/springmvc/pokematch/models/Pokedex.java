package fr.uge.jee.springmvc.pokematch.models;

import fr.uge.jee.springmvc.pokematch.api.IPokeAPI;
import fr.uge.jee.springmvc.pokematch.api.PokeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

public class Pokedex {
    private final IPokeAPI pokeAPI;
    private final int maxSize;
    private final Map<Integer, Pokemon> pokemons = new HashMap<>();

    private Pokedex(IPokeAPI pokeAPI, int maxSize) {
        this.pokeAPI = Objects.requireNonNull(pokeAPI);
        this.maxSize = maxSize;
    }

    /**
     * Add the provided pokemons' list to the pokedex
     * @param pokemons pokemon list to add
     */
    private void addPokemons(List<Pokemon> pokemons) {
        //this.pokemons.addAll(pokemons.stream().limit(Math.min(maxSize, pokemons.size())).collect(Collectors.toList()));
        for(var i = 0; i < pokemons.size() && pokemons.size() < maxSize; i++) {
            var pokemon = pokemons.get(i);
            this.pokemons.put(pokemon.hashCode(), pokemon);
        }
    }

    /**
     * Fill pokemon from the api
     */
    public void fillPokemons() {
        while(pokeAPI.hasNext() && pokemons.size() < maxSize) {
            var answer = pokeAPI.getNext();
            if (answer.isEmpty()) break;
            var pokeresponse = answer.get();
            addPokemons(pokeresponse.getPokemons());
        }
    }

    /**
     * Build the pokedex
     * @param pokeAPI pokemon api manager
     * @param maxSize pokdex maximum size
     * @return Pokedex with all pokemons
     */
    public static Pokedex build(IPokeAPI pokeAPI, int maxSize) {
        var pokedex = new Pokedex(pokeAPI, maxSize);
        pokedex.fillPokemons();
        return pokedex;
    }

    /**
     * Get the nearest pokemon's id from the provided id
     * @param id id to search
     * @return the corresponding pokemon
     */
    private int getPokemonId(int id) {
        var nearest = -1;

        for(int hash: pokemons.keySet()) {
            if(nearest < 0 || Math.abs(id - hash) < nearest)
                nearest = hash;
        }

        return nearest;
    }

    /**
     * Get the Pokemon with the nearest hashcode to id
     * @param id hash to search
     * @return the corresponding Pokemon
     */
    public Pokemon getPokemon(int id) {
        return pokemons.get(getPokemonId(id));
    }

    /**
     * Get all pokemons
     * @return all pokemons
     */
    public List<Pokemon> getPokemons() {
        return new ArrayList<>(pokemons.values());
    }
}
