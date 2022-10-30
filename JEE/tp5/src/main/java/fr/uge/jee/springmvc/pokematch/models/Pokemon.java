package fr.uge.jee.springmvc.pokematch.models;

import fr.uge.jee.springmvc.pokematch.api.IPokeAPI;
import fr.uge.jee.springmvc.pokematch.api.PokeAPI;
import fr.uge.jee.springmvc.pokematch.api.PokeDetails;

import java.util.Objects;
import java.util.Optional;

public class Pokemon {
    private String name;
    private String url;
    private PokeDetails details;
    private PokeAPI pokeAPI;

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return name + " : " + url;
    }

    private boolean getDetails(IPokeAPI pokeAPI) {
        System.out.println("detail url : " + url);
        var response = pokeAPI.getDetails(url);

        if (response.isEmpty())
            return false;

        details = response.get();
        return true;
    }

    public Optional<String> getImage(IPokeAPI pokeAPI) {
        Objects.requireNonNull(pokeAPI);

        if (details == null) {
            if (!getDetails(pokeAPI)) {
                System.out.println("Error - details request");
                return Optional.empty();
            }
        }

        return Optional.of(details.getOfficialFront());
    }
}
