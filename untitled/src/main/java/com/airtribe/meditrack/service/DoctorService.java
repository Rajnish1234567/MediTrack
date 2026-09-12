package com.airtribe.meditrack.service;

import com.airtribe.meditrack.constants.Specialization;
import com.airtribe.meditrack.entity.DataStore;
import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.exception.InvalidDataException;
import com.airtribe.meditrack.util.IdGenerator;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class DoctorService {

    private final DataStore<Doctor> doctorStore;

    public DoctorService(DataStore<Doctor> doctorStore) {
        this.doctorStore = doctorStore;
    }

    public Doctor addDoctor(String name, int age, String phone, String email, Specialization specialization, double consultationFee) {
        Doctor doctor = new Doctor(IdGenerator.nextDoctorId(), name, age, phone, email, specialization, consultationFee);
        doctorStore.add(doctor);
        return doctor;
    }

    public Optional<Doctor> getDoctorById(int id) {
        return doctorStore.getAll()
                .stream()
                .filter(doctor -> doctor.getId() == id)
                .findFirst();
    }

    public List<Doctor> getAllDoctors() {
        return doctorStore.getAll();
    }

    public List<Doctor> searchDoctor(String name) {
        return doctorStore.getAll()
                .stream()
                .filter(doctor -> doctor.getName().equalsIgnoreCase(name))
                .toList();
    }

    public List<Doctor> findBySpecialization(Specialization specialization) {
        return doctorStore.getAll()
                .stream()
                .filter(doctor -> doctor.getSpecialization() == specialization)
                .toList();
    }

    public List<Doctor> getDoctorsSortedByFee() {
        return doctorStore.getAll()
                .stream()
                .sorted(Comparator.comparingDouble(Doctor::getConsultationFee))
                .toList();
    }

    public double getAverageConsultationFee() {
        return doctorStore.getAll()
                .stream()
                .mapToDouble(Doctor::getConsultationFee)
                .average()
                .orElse(0.0);
    }

    public boolean updateDoctorFee(int id, double newFee) {
        Doctor doctor = getDoctorById(id)
                .orElseThrow(() -> new InvalidDataException("Doctor not found: " + id));

        doctor.setConsultationFee(newFee);
        return true;
    }

    public boolean deleteDoctor(int id) {
        Doctor doctor = getDoctorById(id)
                .orElseThrow(() -> new InvalidDataException("Doctor not found: " + id));

        return doctorStore.remove(doctor);
    }
}