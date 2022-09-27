package fr.uge.jee.annotations.messenger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Messenger {
    @Value("#{environment.MESSENGER_TOKEN}")
    private String token;

    public void send(String message){
        System.out.println("Using the super secret token "+token+" to send the message : "+message);
    }
}