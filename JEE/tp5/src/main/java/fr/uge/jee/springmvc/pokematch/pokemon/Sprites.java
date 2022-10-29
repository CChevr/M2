package fr.uge.jee.springmvc.pokematch.pokemon;

public class Sprites {
    private Other other;

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
