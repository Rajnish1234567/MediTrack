package com.airtribe.meditrack.entity;

public abstract class MedicalEntity {

    private final int id;

    protected MedicalEntity(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public abstract String getEntityType();
}