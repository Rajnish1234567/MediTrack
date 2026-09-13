package com.airtribe.meditrack.entity;

public class Address implements Cloneable {

    private String city;

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    private String state;

    public Address(String city, String state) {
        this.city = city;
        this.state = state;
    }

    @Override
    public Address clone() {

        try {
            return (Address) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }
}