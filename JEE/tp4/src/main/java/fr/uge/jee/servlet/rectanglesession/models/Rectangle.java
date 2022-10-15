package fr.uge.jee.servlet.rectanglesession.models;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class Rectangle {
    @NotNull(message = "width can't be empty")
    @Min(value = 0, message = "Width must be positive")
    private Integer width;

    @NotNull(message = "height can't be empty")
    @Min(value = 0, message = "height must be positive")
    private Integer height;

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer area() {
        return height * width;
    }

}
