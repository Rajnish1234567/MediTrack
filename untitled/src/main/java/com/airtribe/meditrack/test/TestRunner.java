package com.airtribe.meditrack.test;

import com.airtribe.meditrack.constants.Specialization;
import com.airtribe.meditrack.entity.*;
import com.airtribe.meditrack.service.PatientService;

public class TestRunner {

    public static void main(String[] args) {

        testPatientCreation();
        testDeepCopy();
        testBilling();
        testDataStore();
        System.out.println("All manual tests completed successfully.");
    }

    private static void testPatientCreation() {
        DataStore<Patient> store = new DataStore<>();
        PatientService service = new PatientService(store);
        Patient patient = service.addPatient("Ramu", 28, "9876543210", "rajnish@gmail.com", "O+", new Address("Delhi", "Delhi"));

        if (!"Ramu".equals(patient.getName())) {
            throw new AssertionError("Patient creation test failed.");
        }
        System.out.println("Patient creation: PASS");
    }

    private static void testDeepCopy() {
        Patient original = new Patient(1, "Rajnish", 28, "9876543210", "test@gmail.com", "O+", new Address("Delhi", "Delhi"));
        Patient copy = original.clone();

        copy.getAddress().setCity("Mumbai");

        if (original.getAddress().getCity().equals(copy.getAddress().getCity())) {
            throw new AssertionError("Deep copy test failed.");
        }
        System.out.println("Patient deep copy: PASS");
    }

    private static void testBilling() {
        Patient patient = new Patient(1, "Rajnish", 28, "9876543210", "test@gmail.com", "O+", null);

        Doctor doctor = new Doctor(2, "Dr. Sharma", 45, "9999999999", "doctor@gmail.com", Specialization.CARDIOLOGIST, 1000);

        Appointment appointment = new Appointment(1, patient, doctor, java.time.LocalDateTime.now());

        Bill bill = new Bill(1, patient, appointment, 1000);

        BillSummary summary = bill.generateBill();
        if (summary.getTotal() <= 1000) {throw new AssertionError("Billing test failed.");
        }
        System.out.println("Billing: PASS");
    }

    private static void testDataStore() {
        DataStore<String> store = new DataStore<>();
        store.add("Java");
        store.add("Spring Boot");

        if (store.size() != 2) {
            throw new AssertionError("DataStore test failed.");
        }
        System.out.println("Generic DataStore: PASS");
    }
}