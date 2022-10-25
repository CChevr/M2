package fr.uge.jee.springmvc.pokematch.models;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

public class PokemonManager {
    // TODO utiliser bean / component
    @Value("${pokematch.pokeapi}")
    private String pokeapi;
    private final Map<Long, Pokemon> pokemons = new HashMap<>();

    public static PokemonManager build() {
        var pokemonManager = new PokemonManager();
        WebClient webClient = WebClient.create();
        var fluxStudent = webClient.get()
                .uri(pokeapi + "pokemon/")
                .retrieve()
                .bodyToMono()

    }

}
