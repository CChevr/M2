package fr.uge.jee.springmvc.pokematch;

import fr.uge.jee.springmvc.pokematch.api.IPokeAPI;
import fr.uge.jee.springmvc.pokematch.api.PokeAPI;
import fr.uge.jee.springmvc.pokematch.models.Pokedex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@PropertySource("classpath:pokematch.properties")
public class Config {
    @Value("${pokematch.pokesize}")
    private int maxSize;

    @Bean
    IPokeAPI pokeAPI() {
        return new PokeAPI();
    }

    @Bean
    Pokedex pokedex(IPokeAPI pokeAPI) {
        return Pokedex.build(pokeAPI, maxSize);
    }

    @Bean
    WebClient getWebClient(WebClient.Builder defaultBuilder) {
        return defaultBuilder.exchangeStrategies(ExchangeStrategies.builder()
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(16 * 1024 * 1024)).build()).build();
    }
}
