package fr.uge.jee.annotations.onlineshop;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
@ComponentScan
public class Config {
    /*
    @Bean
    Delivery standardDelivery() {
        return new ClassicShipping(999);
    }

    @Bean
    Insurance returnInsurance() {
        return new ReturnInsurance(true);
    }

    @Bean
    Insurance thiefInsurance() {
        return new ThiefInsurance();
    }

    @Bean
    OnlineShop onlineShop(Set<Delivery> deliveries, Set<Insurance> insurances) {
        var name = "AhMaZone";
        var os = new OnlineShop();

        os.setName(name);
        os.setInsurance(insurances);
        os.setDeliveryOption(deliveries);

        return os;
    }
     */
}
