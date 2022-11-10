package fr.uge.jee.springmvc.pokematch.models;

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

    public String getImageURL() {
        return imageURL;
    }

    private void storeImage() throws IOException {
        if (null == imageURL) return;
        var url = new URL(this.imageURL);
        InputStream in = new BufferedInputStream(url.openStream());
        image = Base64.getEncoder().encodeToString(in.readAllBytes());
    }

    public String getImage() throws IOException {
        synchronized (name) {
            if (null == image)
                storeImage();
            return image;
        }
    }
}
