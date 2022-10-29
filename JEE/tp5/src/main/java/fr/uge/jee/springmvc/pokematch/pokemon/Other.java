package fr.uge.jee.springmvc.pokematch.pokemon;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Other {
    @JsonProperty("official-artwork")
    private OfficialArtwrok officialArtwrok;

    public void setOfficialArtwrok(OfficialArtwrok officialArtwrok) {
        this.officialArtwrok = officialArtwrok;
    }

    public OfficialArtwrok getOfficialArtwrok() {
        return officialArtwrok;
    }

    public String getOfficialFront() {
        return officialArtwrok.getFrontDefault();
    }

}
