package fr.uge.tp2.models;

public class Drug {
    private final int cip;
    private double price;

    public Drug(int cip, double price) {
        this.cip = cip;
        this.price = price;
    }

    public Drug(int cip) {
        this.cip = cip;
    }

    public int getCip() {
        return cip;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Drug{" +
                "cip=" + cip +
                ", price=" + price +
                '}';
    }
}
