package com.ap.bindkeeper.apnearme;

/**
 * Created by User on 08/06/2016.
 */
public class Place {
    String name, address;

    public Place(String name, String address) {

        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
