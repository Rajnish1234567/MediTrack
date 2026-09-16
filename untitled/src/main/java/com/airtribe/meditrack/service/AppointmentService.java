package com.airtribe.meditrack.service;

import com.airtribe.meditrack.constants.AppointmentStatus;
import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.entity.DataStore;
import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.exception.AppointmentNotFoundException;
import com.airtribe.meditrack.exception.InvalidDataException;
import com.airtribe.meditrack.util.IdGenerator;

import java.time.LocalDateTime;
import java.util.List;

public class AppointmentService {

    private final DataStore<Appointment> appointmentStore;

    public AppointmentService(DataStore<Appointment> appointmentStore) {
        this.appointmentStore = appointmentStore;
    }

    public Appointment createAppointment(Patient patient, Doctor doctor, LocalDateTime appointmentDateTime) {

        Appointment appointment = new Appointment(IdGenerator.nextAppointmentId(), patient, doctor, appointmentDateTime);
        appointmentStore.add(appointment);
        return appointment;
    }

    public Appointment getAppointmentById(int id) {

        return appointmentStore.getAll()
                .stream()
                .filter(appointment -> appointment.getId() == id)
                .findFirst()
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found: " + id));
    }

    public List<Appointment> getAllAppointments() {
        return appointmentStore.getAll();
    }

    public void confirmAppointment(int id) {
        Appointment appointment = getAppointmentById(id);

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new InvalidDataException("Cancelled appointment cannot be confirmed.");
        }
        appointment.confirm();
    }

    public void cancelAppointment(int id) {
        Appointment appointment = getAppointmentById(id);
        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new InvalidDataException("Completed appointment cannot be cancelled.");
        }
        appointment.cancel();
    }

    public void completeAppointment(int id) {
        Appointment appointment = getAppointmentById(id);
        appointment.complete();
    }

    public List<Appointment> getAppointmentsByDoctor(int doctorId) {
        return appointmentStore.getAll()
                .stream()
                .filter(appointment -> appointment.getDoctor().getId() == doctorId)
                .toList();
    }

    public List<Appointment> getAppointmentsByPatient(int patientId) {
        return appointmentStore.getAll()
                .stream()
                .filter(appointment -> appointment.getPatient().getId() == patientId)
                .toList();
    }
}
