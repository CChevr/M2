package fr.uge.jee.springmvc.pokematch.models;

import org.springframework.cache.annotation.Cacheable;
import org.thymeleaf.context.IContext;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;
import java.util.Objects;

public class Pokemon {
    private final String name;
    private final String imageURL;
    private String image;

    public Pokemon(String name, String imageURL) {
        this.name = Objects.requireNonNull(name);
        this.imageURL = imageURL;
    }

    public String getName() {
        return name;
    }

    /*
    public String getImage() {
        return image;
    }
     */

    public String getImageURL() {
        return imageURL;
    }

    private void storeImage() throws IOException {
        var url = new URL(this.imageURL);
        InputStream in = new BufferedInputStream(url.openStream());
        image = Base64.getEncoder().encodeToString(in.readAllBytes());
    }

    @Cacheable(value = "images", key="#target.name")
    public String getImage() throws IOException {
        if (null == image)
            storeImage();
        return image;
    }
}
