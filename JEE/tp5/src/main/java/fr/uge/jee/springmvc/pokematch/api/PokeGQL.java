package fr.uge.jee.springmvc.pokematch.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.uge.jee.springmvc.pokematch.models.Pokemon;
import graphql.kickstart.spring.webclient.boot.GraphQLRequest;
import graphql.kickstart.spring.webclient.boot.GraphQLWebClient;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class PokeGQL implements IPokeAPI {
    private final String uri = "https://beta.pokeapi.co/graphql/v1beta";
    private final WebClient webClient;

    public PokeGQL(WebClient webClient) {
        this.webClient = Objects.requireNonNull(webClient);
    }

    private GraphQLRequest getRequestLimited(int limit) {
        return GraphQLRequest.builder()
                .resource("queryPokemonLimit.graphql")
                .variables(Map.of("RefNumber", limit))
                .build();
    }

    private GraphQLRequest getRequest() {
        return GraphQLRequest.builder()
                .resource("queryPokemon.graphql")
                .build();
    }

    @Override
    public List<Pokemon> getPokemons(int limit) {
        var webClientGQL = webClient.mutate().baseUrl(uri).build();
        GraphQLWebClient graphqlClient = GraphQLWebClient.newInstance(webClientGQL, new ObjectMapper());

        var query = (limit >= 0) ? getRequestLimited(limit) : getRequest();

        var response = graphqlClient.post(query)
                .block();

        if (response != null) {
            return response.getFirstList(PokeDetailsGQL.class).stream()
                    .map(PokeDetailsGQL::buildPokemon)
                    .flatMap(Optional::stream)
                    .collect(Collectors.toList());
        }

        return List.of();
    }
}
