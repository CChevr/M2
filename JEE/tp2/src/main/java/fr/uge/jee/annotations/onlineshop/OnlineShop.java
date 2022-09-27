package fr.uge.jee.annotations.onlineshop;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class OnlineShop {
    private String name;
    private Set<Delivery> deliveryOptions;
    private Set<Insurance> insurances;

    public void setName(String name) {
        this.name = Objects.requireNonNull(name);
    }

    public void setDeliveryOption(Set<Delivery> deliveries) {
        Objects.requireNonNull(deliveries);
        this. deliveryOptions = Set.copyOf(deliveries);
    }

    public void setInsurance(Set<Insurance> insurances) {
        Objects.requireNonNull(insurances);
        this.insurances = Set.copyOf(insurances);
    }

    public void printDescription(){
        System.out.println(name);
        System.out.println("Delivery options");
        deliveryOptions.forEach(opt -> System.out.println("\t"+opt.getDescription()));
        System.out.println("Insurance options");
        insurances.forEach(insurance -> System.out.println("\t"+insurance.getDescription()));
    }
}
