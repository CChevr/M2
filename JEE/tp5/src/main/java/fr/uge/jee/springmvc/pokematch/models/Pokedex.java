package fr.uge.jee.springmvc.pokematch.models;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;
import java.util.stream.Collectors;

public class Pokedex {
    private final String pokeapi;
    private final int maxSize;
    //private final List<Pokemon> pokemons = new ArrayList<>();
    private final Map<Integer, Pokemon> pokemons = new HashMap<>();

    private Pokedex(String api, int maxSize) {
        this.pokeapi = Objects.requireNonNull(api);
        this.maxSize = maxSize;
    }

    /**
     * Ask to the api
     * @param uri uri to question
     * @return api response
     */
    private Optional<PokeResponse> askApi(String uri) {
        WebClient webClient = WebClient.create();

        var monoClient = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(PokeResponse.class);

        return monoClient.blockOptional();
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
        var uri = pokeapi;

        while(pokemons.size() < maxSize) {
            var answer = askApi(uri);
            if (answer.isEmpty()) break;
            var pokeresponse = answer.get();
            uri = pokeresponse.getNext();
            addPokemons(pokeresponse.getPokemons());
        }
    }

    /**
     * Build the pokedex
     * @param api pokemon api
     * @param maxSize pokdex maximum size
     * @return Pokedex with all pokemons
     */
    public static Pokedex build(String api, int maxSize) {
        var pokedex = new Pokedex(api, maxSize);
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
