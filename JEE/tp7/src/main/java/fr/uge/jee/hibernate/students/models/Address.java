package fr.uge.jee.hibernate.students.models;

import javax.persistence.*;

@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue
    @Column(name = "addressid")
    private int id;
    private int streetNumber;
    private String street;
    private String city;

    public int getId() {
        return id;
    }

    public int getStreetNumber() {
        return streetNumber;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStreetNumber(int streetNumber) {
        this.streetNumber = streetNumber;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
