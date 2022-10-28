package fr.uge.jee.springmvc.pokematch.api;

import java.util.Optional;

public interface IPokeAPI {
    boolean hasNext();
    Optional<IPokeResponse> getNext();
}
