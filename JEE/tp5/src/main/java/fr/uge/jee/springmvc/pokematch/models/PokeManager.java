package fr.uge.jee.springmvc.pokematch.models;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

public class PokeManager {
    // TODO utiliser bean / component
    @Value("${pokematch.pokeapi}")
    private static String pokeapi;
    private final Map<Long, Pokemon> pokemons = new HashMap<>();

    public static PokeManager build() {
        var pokemonManager = new PokeManager();
        WebClient webClient = WebClient.create();
        var monoClient = webClient.get()
                .uri(pokeapi + "pokemon/")
                .retrieve()
                .bodyToMono(PokeResponse.class);


        monoClient.block()
    }

}
