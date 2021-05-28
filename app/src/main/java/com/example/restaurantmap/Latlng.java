package com.example.restaurantmap;

import java.io.Serializable;

public class Latlng implements Serializable {
    private Double lat;
    private Double lng;
    private String name;
    public Latlng(Double _lat, Double _lng, String _name){
        lat = _lat;
        lng = _lng;
        name = _name;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }

    public String getName(){
        return name;
    }
}
