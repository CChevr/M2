package fr.uge.jee.springmvc.pokematch.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

public class PokeAPI implements IPokeAPI {
    @Value("${pokematch.pokeapi}")
    private String next;
    private final WebClient webClient;

    public PokeAPI(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public boolean hasNext() {
        return next != null;
    }

    // TODO effectue les appelles les uns après les autres plutot que de les paralelliser
    /**
     * Ask to the api
     * @param uri uri to question
     * @return api response
     */
    private Optional<IPokeResponse> askApi(String uri) {
        var monoClient = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(PokeResponse.class);

        return Optional.ofNullable(monoClient.block());
    }

    @Override
    public Optional<IPokeResponse> getNext() {
        if (!hasNext()) return Optional.empty();

        var answer = askApi(next);
        if (answer.isPresent()) {
            var pokeresponse = answer.get();
            next = pokeresponse.getNext();
        }
        return answer;
    }

    @Override
    public Optional<PokeDetails> getDetails(String uri) {
        var monoClient = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(PokeDetails.class);

        return Optional.ofNullable(monoClient.block());
    }

    // TODO getAll -> faire attention à paralléliser les requêtes
}
