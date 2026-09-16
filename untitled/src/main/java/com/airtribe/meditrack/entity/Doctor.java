package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.constants.Specialization;
import com.airtribe.meditrack.exception.InvalidDataException;

public class Doctor extends Person {

    private Specialization specialization;
    private double consultationFee;

    public Doctor(int id, String name, int age, String phone, String email, Specialization specialization, double consultationFee) {
        super(id, name, age, phone, email);

        if (specialization == null) {
            throw new InvalidDataException("Specialization cannot be null");
        }
        if (consultationFee <= 0) {
            throw new InvalidDataException("Consultation fee must be greater than zero");
        }

        this.specialization = specialization;
        this.consultationFee = consultationFee;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    public double getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(double consultationFee) {
        this.consultationFee = consultationFee;
    }

    @Override
    public String getEntityType() {
        return "DOCTOR";
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", specialization=" + specialization +
                ", fee=" + consultationFee +
                '}';
    }
}