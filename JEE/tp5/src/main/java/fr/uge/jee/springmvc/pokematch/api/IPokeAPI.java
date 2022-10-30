package fr.uge.jee.springmvc.pokematch.api;

import fr.uge.jee.springmvc.pokematch.models.Pokemon;

import java.util.List;
import java.util.Optional;

public interface IPokeAPI {
    List<Pokemon> getPokemons(int limit);
}
