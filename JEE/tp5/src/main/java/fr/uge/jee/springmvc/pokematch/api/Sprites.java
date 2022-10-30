package fr.uge.jee.springmvc.pokematch.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Sprites {
    private Other other;

    private static class Other {
        @JsonProperty("official-artwork")
        private Other.OfficialArtwork officialArtwork;

        private static class OfficialArtwork {
            @JsonProperty("front_default")
            private String frontDefault;

            public String getFrontDefault() {
                return frontDefault;
            }

            public void setFrontDefault(String frontDefault) {
                this.frontDefault = frontDefault;
            }
        }

        public void setOfficialArtwork(Other.OfficialArtwork officialArtwork) {
            this.officialArtwork = officialArtwork;
        }

        public Other.OfficialArtwork getOfficialArtwork() {
            return officialArtwork;
        }

        public String getOfficialFront() {
            return officialArtwork.getFrontDefault();
        }

    }

    public Other getOther() {
        return other;
    }

    public void setOther(Other other) {
        this.other = other;
    }

    public String getOfficialFront() {
        return other.getOfficialFront();
    }
}
