package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.constants.AppointmentStatus;
import com.airtribe.meditrack.exception.InvalidDataException;
import com.airtribe.meditrack.util.DateUtil;

import java.time.LocalDateTime;
import java.util.Objects;

public class Appointment implements Cloneable {

    private final int id;
    private Patient patient;
    private Doctor doctor;
    private LocalDateTime appointmentDateTime;
    private AppointmentStatus status;

    public Appointment(int id, Patient patient, Doctor doctor, LocalDateTime appointmentDateTime) {

        if (patient == null) {
            throw new InvalidDataException("Patient cannot be null.");
        }
        if (doctor == null) {
            throw new InvalidDataException("Doctor cannot be null.");
        }
        if (appointmentDateTime == null) {
            throw new InvalidDataException("Appointment date cannot be null.");
        }

        this.id = id;
        this.patient = patient;
        this.doctor = doctor;
        this.appointmentDateTime = appointmentDateTime;
        this.status = AppointmentStatus.PENDING;
    }

    public int getId() {
        return id;
    }

    public Patient getPatient() {
        return patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public LocalDateTime getAppointmentDateTime() {
        return appointmentDateTime;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void confirm() {
        this.status = AppointmentStatus.CONFIRMED;
    }

    public void cancel() {
        this.status = AppointmentStatus.CANCELLED;
    }

    public void complete() {
        this.status = AppointmentStatus.COMPLETED;
    }

    @Override
    public Appointment clone() {

        try {

            Appointment cloned = (Appointment) super.clone();

            // Deep copy Patient
            if (this.patient != null) {
                cloned.patient = this.patient.clone();
            }

            // Doctor is not Cloneable,
            // therefore create a new Doctor object.
            if (this.doctor != null) {
                cloned.doctor = new Doctor(
                        this.doctor.getId(),
                        this.doctor.getName(),
                        this.doctor.getAge(),
                        this.doctor.getPhone(),
                        this.doctor.getEmail(),
                        this.doctor.getSpecialization(),
                        this.doctor.getConsultationFee()
                );
            }

            return cloned;

        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Unable to clone Appointment", e);
        }
    }

    @Override
    public String toString() {

        return "Appointment{" +
                "id=" + id +
                ", patient=" + patient.getName() +
                ", doctor=" + doctor.getName() +
                ", dateTime=" +
                DateUtil.format(appointmentDateTime) +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Appointment that)) {
            return false;
        }
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}