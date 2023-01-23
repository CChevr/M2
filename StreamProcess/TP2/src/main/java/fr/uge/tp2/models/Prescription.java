package fr.uge.tp2.models;

import com.fasterxml.jackson.annotation.JsonProperty;

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
}
