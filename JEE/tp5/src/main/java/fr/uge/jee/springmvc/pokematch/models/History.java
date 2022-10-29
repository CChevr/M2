package fr.uge.jee.springmvc.pokematch.models;

import fr.uge.jee.springmvc.pokematch.pokemon.Pokemon;

import java.util.ArrayList;
import java.util.List;

public class History {
    private final List<Pokemon> pokemons = new ArrayList<>();

    public void addComputation(Pokemon pokemon){
        pokemons.add(pokemon);
    }
    public boolean isEmpty(){
        return pokemons.isEmpty();
    }
    public Pokemon getLatest(){
        return pokemons.get(pokemons.size()-1);
    }
    public List<Pokemon> getPastComputations(){
        return List.copyOf(pokemons);
    }
}
