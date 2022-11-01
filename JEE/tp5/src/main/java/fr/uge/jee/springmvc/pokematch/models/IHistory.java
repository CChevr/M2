package fr.uge.jee.springmvc.pokematch.models;

import java.util.List;

public interface IHistory {
    void addComputation(Pokemon pokemon);

    List<Pokemon> getLastPokemons(int size);

    boolean isEmpty();

    List<Pokemon> getPastComputations();
}
