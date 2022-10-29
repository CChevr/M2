package fr.uge.jee.springmvc.pokematch.api;

import fr.uge.jee.springmvc.pokematch.models.PokeDetails;

import java.util.Optional;

public interface IPokeAPI {
    boolean hasNext();
    Optional<IPokeResponse> getNext();
    Optional<PokeDetails> getDetails(String uri);
}
