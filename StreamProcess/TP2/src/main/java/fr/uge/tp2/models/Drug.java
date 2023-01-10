package fr.uge.tp2.models;

public class Drug {
    private final int cip;
    private final double price;

    public Drug(int cip, double price) {
        this.cip = cip;
        this.price = price;
    }

    public int getCip() {
        return cip;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Drug{" +
                "cip=" + cip +
                ", price=" + price +
                '}';
    }
}
