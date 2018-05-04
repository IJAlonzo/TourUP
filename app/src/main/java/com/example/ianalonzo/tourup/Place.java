package com.example.ianalonzo.tourup;

public class Place {
    String name;
    String address;
    String lat;
    String lng;
    String reference;
    String image;
    String rating;


    public Place() {

    }

    public Place(String name, String address, String lat, String lng, String reference, String image, String rating) {
        this.name = name;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.reference = reference;
        this.image = image;
        this.rating = rating;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public String getReference() {
        return reference;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
