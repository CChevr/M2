package fr.uge.jee.springmvc.pokematch.models;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.stream.Collectors;

public class History implements IHistory {
    private final Map<String, Data> history = new HashMap<>();

    private static class Data {
        private final Pokemon pokemon;
        private int count;

        public Data(Pokemon pokemon) {
            this.pokemon = pokemon;
            this.count = 0;
        }

        public Pokemon getPokemon() {
            return pokemon;
        }

        public int getCount() {
            return count;
        }

        public void increase() {
            count++;
        }

        public static Data merge(Data a, Data b) {
            if (null == a) return b;
            if (null == b) return a;
            if (a.getPokemon() == b.getPokemon()) {
                a.increase();
                return a;
            }
            return b;
        }
    }

    public void addComputation(Pokemon pokemon){
        history.merge(pokemon.getName(), new Data(pokemon), Data::merge);
    }

    private static int lastComporator(Map.Entry<String, Data> a, Map.Entry<String, Data> b) {
        return b.getValue().getCount() - a.getValue().getCount();
    }

    public List<Pokemon> getLastPokemons(int size) {
        return history.entrySet().stream()
                .sorted(History::lastComporator)
                .map(x -> x.getValue().getPokemon())
                .limit(size)
                .collect(Collectors.toList());
    }

    public boolean isEmpty(){
        return history.isEmpty();
    }

    public List<Pokemon> getPastComputations(){
        return history.values().stream().map(Data::getPokemon).collect(Collectors.toList());
    }
}
