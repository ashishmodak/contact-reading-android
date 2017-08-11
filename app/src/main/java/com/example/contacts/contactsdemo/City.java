package com.example.contacts.contactsdemo;

import java.util.ArrayList;

/**
 * Created by muffin on 11/08/17.
 */

public class City {
    public String cityName;
    public String cityCountry;

    public City() {
        cityName = cityCountry = "";
    }

    public City(String cityName, String cityCountry) {
        this.cityName = cityName;
        this.cityCountry = cityCountry;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityCountry() {
        return cityCountry;
    }

    public void setCityCountry(String cityCountry) {
        this.cityCountry = cityCountry;
    }
}
