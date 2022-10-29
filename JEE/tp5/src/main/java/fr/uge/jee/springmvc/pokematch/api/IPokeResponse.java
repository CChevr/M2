package fr.uge.jee.springmvc.pokematch.api;

import fr.uge.jee.springmvc.pokematch.pokemon.Pokemon;

import java.util.List;

public interface IPokeResponse {
    List<Pokemon> getPokemons();
    String getNext();
    String getPrevious();
    int getCount();
}
