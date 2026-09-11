package com.airtribe.meditrack.entity;

public abstract class Person extends MedicalEntity {

    private String name;
    private int age;
    private String phone;
    private String email;

    protected Person(int id, String name, int age, String phone, String email) {
        super(id);
        this.name = name;
        this.age = age;
        this.phone = phone;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        Validator.validateName(name);
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        Validator.validateAge(age);
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        Validator.validatePhone(phone);
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        Validator.validateEmail(email);
        this.email = email;
    }
}