package fr.uge.jee.springmvc.pokematch.api;

import fr.uge.jee.springmvc.pokematch.models.Pokemon;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class PokeAPI implements IPokeAPI {
    private String url = "https://pokeapi.co/api/v2/pokemon/";
    private final WebClient webClient;

    public PokeAPI(WebClient webClient) {
        this.webClient = webClient;
    }

    private List<Pokemon> readListResponse(PokeListResponse plr) {
        var pokemons = new ArrayList<Pokemon>();

        var monos = plr.getResponses().stream()
                .map(r -> webClient.get()
                    .uri(r.getUrl())
                    .retrieve()
                    .bodyToMono(PokeDetails.class))
                .collect(Collectors.toList());

        Flux.merge(monos).toStream()
                .map(PokeDetails::buildPokemon)
                .flatMap(Optional::stream)
                .forEach(pokemons::add);

        return pokemons;
    }

    public List<Pokemon> getPokemons(int limit) {
        if (limit != 0) {
            var monoLstRsp = webClient.get()
                    .uri(url + "?offset=0&limit=" + limit)
                    .retrieve()
                    .bodyToMono(PokeListResponse.class);

            var lstRsp = monoLstRsp.block();
            if (lstRsp != null) {
                return readListResponse(lstRsp);
            }
        }

        return List.of();
    }
}
