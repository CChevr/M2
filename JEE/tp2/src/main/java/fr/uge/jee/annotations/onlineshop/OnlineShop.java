package fr.uge.jee.annotations.onlineshop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;

@Component
public class OnlineShop {
    @Value("${onlineshop.name}")
    private String name;
    @Autowired
    private Set<Delivery> deliveryOptions;
    @Autowired
    private Set<Insurance> insurances;

    /*
    OnlineShop(@Value("AhMaZone")String name) {
        this.name = Objects.requireNonNull(name);
    }
    */

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
