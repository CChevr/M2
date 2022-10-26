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
    private List<Pokemon> pokemons = new ArrayList<>();
    //private final Map<Long, Pokemon> pokemons = new HashMap<>();

    private Pokedex(String api) {
        this.pokeapi = Objects.requireNonNull(api);
    }

    public static Pokedex build(String api) {
        var pokedex = new Pokedex(api);
        System.out.println(pokedex.pokeapi);

        //WebClient webClient = WebClient.create();
        /*
        var monoClient = webClient.get()
                .uri(pokeapi)
                .retrieve()
                .bodyToMono(PokeResponse.class);


        var pokeresponse = monoClient.blockOptional();
        pokeresponse.ifPresent(x -> pokeManager.setPokemons(x.getPokemons()));
         */
        return pokedex;
    }

    public void setPokemons(List<Pokemon> pokemons) {
        this.pokemons = pokemons;
    }

    public List<Pokemon> getPokemons() {
        return pokemons;
    }

    public String getPokeapi() {
        return pokeapi;
    }
}
