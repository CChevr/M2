package fr.uge.jee.springmvc.pokematch.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.uge.jee.springmvc.pokematch.models.Pokemon;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PokeListResponse {
    @JsonProperty("results")
    private List<PokeResponse> responses;

    public List<PokeResponse> getResponses() {
        return responses;
    }

    public void setResponses(List<PokeResponse> responses) {
        this.responses = responses;
    }
}
