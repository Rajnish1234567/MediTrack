package com.airtribe.meditrack.entity;

public class Patient extends Person implements Cloneable {

    private String bloodGroup;
    private Address address;

    public Patient(int id, String name, int age, String phone, String email, String bloodGroup, Address address) {

        super(id, name, age, phone, email);
        this.bloodGroup = bloodGroup;
        this.address = address;
    }

    @Override
    public Patient clone() {
        try {
            Patient cloned = (Patient) super.clone();

            if (this.address != null) {
                cloned.address = this.address.clone();
            }
            return cloned;

        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    @Override
    public String getEntityType() {
        return "PATIENT";
    }
}