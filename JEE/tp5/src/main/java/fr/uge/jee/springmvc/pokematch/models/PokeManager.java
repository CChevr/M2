package fr.uge.jee.springmvc.pokematch.models;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PokeManager {

    @Value("${pokematch.pokeapi}")
    private static String pokeapi;
    private List<Pokemon> pokemons = new ArrayList<>();
    //private final Map<Long, Pokemon> pokemons = new HashMap<>();

    public static PokeManager build() {
        var pokeManager = new PokeManager();
        WebClient webClient = WebClient.create();
        var monoClient = webClient.get()
                .uri(pokeapi)
                .retrieve()
                .bodyToMono(PokeResponse.class);


        var pokeresponse = monoClient.blockOptional();
        pokeresponse.ifPresent(x -> pokeManager.setPokemons(x.getPokemons()));
        return pokeManager;
    }

    public static void setPokeapi(String pokeapi) {
        PokeManager.pokeapi = pokeapi;
    }

    public void setPokemons(List<Pokemon> pokemons) {
        this.pokemons = pokemons;
    }

    public static String getPokeapi() {
        return pokeapi;
    }

    public List<Pokemon> getPokemons() {
        return pokemons;
    }
}
