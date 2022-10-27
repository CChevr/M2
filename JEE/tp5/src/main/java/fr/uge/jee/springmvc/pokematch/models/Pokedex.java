package fr.uge.jee.springmvc.pokematch.models;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class Pokedex {
    private final String pokeapi;
    private final int maxSize;
    private final List<Pokemon> pokemons = new ArrayList<>();
    //private final Map<Long, Pokemon> pokemons = new HashMap<>();

    private Pokedex(String api, int maxSize) {
        this.pokeapi = Objects.requireNonNull(api);
        this.maxSize = maxSize;
    }

    private Optional<PokeResponse> askApi(String uri) {
        WebClient webClient = WebClient.create();

        var monoClient = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(PokeResponse.class);

        return monoClient.blockOptional();
    }

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

    public static Pokedex build(String api, int maxSize) {
        var pokedex = new Pokedex(api, maxSize);
        pokedex.fillPokemons();
        return pokedex;
    }

    public void addPokemons(List<Pokemon> pokemons) {
        this.pokemons.addAll(pokemons.stream().limit(Math.min(maxSize, pokemons.size())).collect(Collectors.toList()));
    }

    public List<Pokemon> getPokemons() {
        return pokemons;
    }
}
