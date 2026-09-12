package com.airtribe.meditrack.service;

import com.airtribe.meditrack.entity.Address;
import com.airtribe.meditrack.entity.DataStore;
import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.util.IdGenerator;

import java.util.List;
import java.util.Optional;

public class PatientService {

    private final DataStore<Patient> patientStore;

    public PatientService(DataStore<Patient> patientStore) {
        this.patientStore = patientStore;
    }

    public Patient addPatient(String name, int age, String phone, String email, String bloodGroup, Address address) {
        Patient patient = new Patient(IdGenerator.nextPatientId(), name, age, phone, email, bloodGroup, address);
        patientStore.add(patient);
        return patient;
    }

    public Optional<Patient> getPatientById(int id) {
        return patientStore.getAll()
                .stream()
                .filter(patient -> patient.getId() == id)
                .findFirst();
    }

    public List<Patient> searchPatient(String name) {
        return patientStore.getAll()
                .stream()
                .filter(patient -> patient.getName().equalsIgnoreCase(name))
                .toList();
    }

    public List<Patient> searchPatient(int age) {
        return patientStore.getAll()
                .stream()
                .filter(patient -> patient.getAge() == age)
                .toList();
    }
}