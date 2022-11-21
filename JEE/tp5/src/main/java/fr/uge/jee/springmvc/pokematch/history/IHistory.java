package fr.uge.jee.springmvc.pokematch.history;

import fr.uge.jee.springmvc.pokematch.models.Pokemon;

import java.util.List;

public interface IHistory {
    void addComputation(Pokemon pokemon);

    List<Pokemon> getLastPokemons(int size);

    boolean isEmpty();

    List<Pokemon> getPastComputations();
}
