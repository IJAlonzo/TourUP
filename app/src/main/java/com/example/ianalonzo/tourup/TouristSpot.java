package com.example.ianalonzo.tourup;

/**
 * Created by Asus on 4/2/2018.
 */

public class TouristSpot {

    private int name;
    private int description;
    private int image;

    public TouristSpot(int name, int description, int image) {
        this.name = name;
        this.description = description;
        this.image = image;
    }

    public int getName() {
        return name;
    }

    public int getDescription() {
        return description;
    }

    public int getImage() {
        return image;
    }
}
