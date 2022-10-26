package fr.uge.jee.springmvc.pokematch.models;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Pokedex {
    private String pokeapi;
    private final List<Pokemon> pokemons = new ArrayList<>();
    //private final Map<Long, Pokemon> pokemons = new HashMap<>();

    private Pokedex(String api) {
        this.pokeapi = Objects.requireNonNull(api);
    }

    public void fillPokemons() {
        WebClient webClient = WebClient.create();
        var monoClient = webClient.get()
                .uri(pokeapi)
                .retrieve()
                .bodyToMono(PokeResponse.class);


        var pokeresponse = monoClient.blockOptional();
        pokeresponse.ifPresent(x -> addPokemons(x.getPokemons()));
    }

    public static Pokedex build(String api) {
        var pokedex = new Pokedex(api);
        pokedex.fillPokemons();
        return pokedex;
    }

    public void addPokemons(List<Pokemon> pokemons) {
        this.pokemons.addAll(pokemons);
    }

    public List<Pokemon> getPokemons() {
        return pokemons;
    }
}
