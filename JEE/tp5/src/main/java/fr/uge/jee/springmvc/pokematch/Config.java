package fr.uge.jee.springmvc.pokematch;

import fr.uge.jee.springmvc.pokematch.api.IPokeAPI;
import fr.uge.jee.springmvc.pokematch.api.PokeGQL;
import fr.uge.jee.springmvc.pokematch.history.History;
import fr.uge.jee.springmvc.pokematch.history.IHistory;
import fr.uge.jee.springmvc.pokematch.models.Pokedex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@PropertySource("classpath:pokematch.properties")
//@EnableCaching
public class Config {
    @Value("${pokematch.pokesize}")
    private int maxSize;

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("images");
    }

    @Bean
    WebClient getWebClient(WebClient.Builder defaultBuilder) {
        return defaultBuilder.exchangeStrategies(ExchangeStrategies.builder()
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(16 * 1024 * 1024)).build()).build();
    }

    @Bean
    IPokeAPI pokeAPI(WebClient getWebClient) {
        //return new PokeAPI(getWebClient);
        return new PokeGQL(getWebClient);
    }

    @Bean
    IHistory history() { return new History(); }

    @Bean
    Pokedex pokedex(IPokeAPI pokeAPI) {
        return Pokedex.build(pokeAPI, maxSize);
    }
}
