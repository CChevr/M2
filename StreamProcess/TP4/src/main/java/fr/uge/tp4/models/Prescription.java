package fr.uge.tp4.models;

public class Prescription {
    private String nom;
    private String prenom;
    private int cip;
    private double prix;
    private int idPharma;

    public Prescription() {}

    public Prescription(String nom, String prenom, int cip, double prix, int idPharma) {
        this.nom = nom;
        this.prenom = prenom;
        this.cip = cip;
        this.prix = prix;
        this.idPharma = idPharma;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public int getCip() {
        return cip;
    }

    public void setCip(int cip) {
        this.cip = cip;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public int getIdPharma() {
        return idPharma;
    }

    public void setIdPharma(int idPharma) {
        this.idPharma = idPharma;
    }

    @Override
    public String toString() {
        return "Prescription{" +
                "nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", cip=" + cip +
                ", prix=" + prix +
                ", idPharma=" + idPharma +
                '}';
    }
}
