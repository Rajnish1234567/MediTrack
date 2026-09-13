package com.airtribe.meditrack.util;
public class IdGenerator {

    private static int patientId;
    private static int doctorId;
    private static int appointmentId;
    private static int billId;

    static {
        patientId = 1000;
        doctorId = 2000;
        appointmentId = 3000;
        billId = 4000;

        System.out.println("IdGenerator initialized");
    }

    private IdGenerator() {
    }

    public static int nextPatientId() {
        return ++patientId;
    }

    public static int nextDoctorId() {
        return ++doctorId;
    }

    public static int nextAppointmentId() {
        return ++appointmentId;
    }

    public static int nextBillId() {
        return ++billId;
    }
}