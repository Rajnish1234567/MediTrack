package com.airtribe.meditrack;

import com.airtribe.meditrack.constants.Constants;
import com.airtribe.meditrack.constants.Specialization;
import com.airtribe.meditrack.entity.*;
import com.airtribe.meditrack.exception.AppointmentNotFoundException;
import com.airtribe.meditrack.exception.InvalidDataException;
import com.airtribe.meditrack.service.AppointmentService;
import com.airtribe.meditrack.service.DoctorService;
import com.airtribe.meditrack.service.PatientService;
import com.airtribe.meditrack.util.CSVUtil;
import com.airtribe.meditrack.util.DateUtil;
import com.airtribe.meditrack.util.IdGenerator;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    private static final DataStore<Patient> patientStore = new DataStore<>();
    private static final DataStore<Doctor> doctorStore = new DataStore<>();
    private static final DataStore<Appointment> appointmentStore = new DataStore<>();

    private static final PatientService patientService = new PatientService(patientStore);
    private static final DoctorService doctorService = new DoctorService(doctorStore);
    private static final AppointmentService appointmentService = new AppointmentService(appointmentStore);

    public static void main(String[] args) {
        System.out.println("=========WELCOME TO MEDI-TRACK ===========");

        runApplication();
        scanner.close();
        System.out.println("\nThank you for using MediTrack.");
    }

    // ==========================================================
    // MAIN APPLICATION LOOP
    // ==========================================================

    private static void runApplication() {
        boolean running = true;

        while (running) {
            printMainMenu();
            int choice = readInt("Enter your choice: ");

            try {
                switch (choice) {
                    case 1 -> patientMenu();
                    case 2 -> doctorMenu();
                    case 3 -> appointmentMenu();
                    case 4 -> billingMenu();
                    case 5 -> searchMenu();
                    case 6 -> analyticsMenu();
                    case 7 -> saveData();
                    case 8 -> loadData();
                    case 0 -> running = false;
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            } catch (InvalidDataException | AppointmentNotFoundException e) {
                System.out.println("\nError: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("\nUnexpected error: " + e.getMessage());
            }
        }
    }

    // ==========================================================
    // MAIN MENU
    // ==========================================================

    private static void printMainMenu() {
        System.out.println("==================MEDI-TRACK Management===============");
        System.out.println("1. Patient Management");
        System.out.println("2. Doctor Management");
        System.out.println("3. Appointment Management");
        System.out.println("4. Billing");
        System.out.println("5. Search");
        System.out.println("6. Analytics");
        System.out.println("7. Save Data");
        System.out.println("8. Load Data");
        System.out.println("0. Exit");
        System.out.println("==========================================");
    }

    // ==========================================================
    // PATIENT MENU
    // ==========================================================

    private static void patientMenu() {
        boolean back = false;

        while (!back) {
            System.out.println("\n----------- PATIENT MANAGEMENT -----------");
            System.out.println("1. Add Patient");
            System.out.println("2. View Patient");
            System.out.println("3. Update Patient");
            System.out.println("4. Delete Patient");
            System.out.println("5. List All Patients");
            System.out.println("6. Search Patient by Name");
            System.out.println("7. Search Patient by Age");
            System.out.println("8. Clone Patient");
            System.out.println("0. Back");

            int choice = readInt("Enter your choice: ");

            try {
                switch (choice) {
                    case 1 -> addPatient();
                    case 2 -> viewPatient();
                    case 3 -> updatePatient();
                    case 4 -> deletePatient();
                    case 5 -> listPatients();
                    case 6 -> searchPatientByName();
                    case 7 -> searchPatientByAge();
                    case 8 -> clonePatient();
                    case 0 -> back = true;
                    default -> System.out.println("Invalid choice.");
                }
            } catch (InvalidDataException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void addPatient() {
        System.out.println("\n----------- ADD PATIENT -----------");
        String name = readString("Enter name: ");
        int age = readInt("Enter age: ");
        String phone = readString("Enter phone: ");
        String email = readString("Enter email: ");
        String bloodGroup = readString("Enter blood group: ");
        String city = readString("Enter city: ");
        String state = readString("Enter state: ");
        Address address = new Address(city, state);

        Patient patient = patientService.addPatient(name, age, phone, email, bloodGroup, address);
        System.out.println("Patient created successfully.");
        System.out.println(patient);
    }

    private static void viewPatient() {
        int id = readInt("Enter patient ID: ");
        Patient patient = patientService.getPatientById(id)
                        .orElseThrow(() -> new InvalidDataException("Patient not found: " + id));

        System.out.println("\n" + patient);
    }

    private static void updatePatient() {
        int id = readInt("Enter patient ID: ");
        String name = readString("Enter new name: ");
        int age = readInt("Enter new age: ");
        String phone = readString("Enter new phone: ");
        String email = readString("Enter new email: ");

        patientService.updatePatient(id, name, age, phone, email);
        System.out.println("Patient updated successfully.");
    }

    private static void deletePatient() {
        int id = readInt("Enter patient ID: ");
        Patient patient = patientService.getPatientById(id)
                .orElseThrow(() -> new InvalidDataException("Patient not found: " + id));

        System.out.println("\nPatient:");
        System.out.println(patient);

        String confirmation = readString("Are you sure you want to delete? (Y/N): ");
        if (confirmation.equalsIgnoreCase("Y")) {
            patientService.removePatientById(id);
            System.out.println("Patient deleted successfully.");
        } else {
            System.out.println("Delete operation cancelled.");
        }
    }

    private static void listPatients() {
        List<Patient> patients = patientService.getAllPatient();

        if (patients.isEmpty()) {
            System.out.println("No patients found.");
            return;
        }
        System.out.println("\n----------- PATIENTS -----------");
        patients.forEach(System.out::println);
        System.out.println("Total patients: " + patients.size());
    }

    private static void searchPatientByName() {
        String name = readString("Enter patient name: ");
        List<Patient> patients = patientService.searchPatient(name);

        if (patients.isEmpty()) {
            System.out.println("No patient found.");
            return;
        }
        patients.forEach(System.out::println);
    }

    private static void searchPatientByAge() {
        int age = readInt("Enter patient age: ");
        List<Patient> patients = patientService.searchPatient(age);

        if (patients.isEmpty()) {
            System.out.println("No patient found.");
            return;
        }
        patients.forEach(System.out::println);
    }

    private static void clonePatient() {
        int id = readInt("Enter patient ID: ");
        Patient original = patientService.getPatientById(id)
                        .orElseThrow(() -> new InvalidDataException("Patient not found: " + id));

        Patient cloned = original.clone();

        System.out.println("\nOriginal:");
        System.out.println(original);

        System.out.println("\nCloned:");
        System.out.println(cloned);

        System.out.println("\nDeep copy created successfully.");
    }

    // ==========================================================
    // DOCTOR MENU
    // ==========================================================

    private static void doctorMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n------------ DOCTOR MANAGEMENT ------------");
            System.out.println("1. Add Doctor");
            System.out.println("2. View Doctor");
            System.out.println("3. Update Consultation Fee");
            System.out.println("4. Delete Doctor");
            System.out.println("5. List All Doctors");
            System.out.println("6. Search by Name");
            System.out.println("7. Search by Specialization");
            System.out.println("8. Sort Doctors by Fee");
            System.out.println("0. Back");

            int choice = readInt("Enter your choice: ");

            try {
                switch (choice) {
                    case 1 -> addDoctor();
                    case 2 -> viewDoctor();
                    case 3 -> updateDoctorFee();
                    case 4 -> deleteDoctor();
                    case 5 -> listDoctors();
                    case 6 -> searchDoctorByName();
                    case 7 -> searchDoctorBySpecialization();
                    case 8 -> sortDoctorsByFee();
                    case 0 -> back = true;
                    default -> System.out.println("Invalid choice.");
                }

            } catch (InvalidDataException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void addDoctor() {
        System.out.println("\n----------- ADD DOCTOR -----------");
        String name = readString("Enter doctor name: ");
        int age = readInt("Enter age: ");
        String phone = readString("Enter phone: ");
        String email = readString("Enter email: ");
        Specialization specialization = selectSpecialization();
        double fee = readDouble("Enter consultation fee: ");

        Doctor doctor = doctorService.addDoctor(name, age, phone, email, specialization, fee);
        System.out.println("\nDoctor created successfully.");
        System.out.println(doctor);
    }

    private static void viewDoctor() {
        int id = readInt("Enter doctor ID: ");
        Doctor doctor = doctorService.getDoctorById(id)
                        .orElseThrow(() -> new InvalidDataException("Doctor not found: " + id));

        System.out.println("\n" + doctor);
    }

    private static void updateDoctorFee() {
        int id = readInt("Enter doctor ID: ");
        double fee = readDouble("Enter new consultation fee: ");
        doctorService.updateDoctorFee(id, fee);

        System.out.println("Consultation fee updated successfully.");
    }

    private static void deleteDoctor() {
        int id = readInt("Enter doctor ID: ");

        Doctor doctor = doctorService.getDoctorById(id)
                        .orElseThrow(() -> new InvalidDataException("Doctor not found: " + id));

        System.out.println(doctor);
        String confirmation = readString("Are you sure? (Y/N): ");

        if (confirmation.equalsIgnoreCase("Y")) {
            doctorService.deleteDoctor(id);
            System.out.println("Doctor deleted successfully.");

        } else {
            System.out.println("Delete operation cancelled.");
        }
    }

    private static void listDoctors() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        if (doctors.isEmpty()) {
            System.out.println("No doctors found.");
            return;
        }

        System.out.println("\n------------ DOCTORS ------------");
        doctors.forEach(System.out::println);

        System.out.println("Total doctors: " + doctors.size());
    }

    private static void searchDoctorByName() {
        String name = readString("Enter doctor name: ");
        List<Doctor> doctors = doctorService.searchDoctor(name);

        if (doctors.isEmpty()) {
            System.out.println("No doctors found.");
            return;
        }
        doctors.forEach(System.out::println);
    }

    private static void searchDoctorBySpecialization() {
        Specialization specialization = selectSpecialization();
        List<Doctor> doctors = doctorService.findBySpecialization(specialization);

        if (doctors.isEmpty()) {
            System.out.println("No doctors found.");
            return;
        }
        doctors.forEach(System.out::println);
    }

    private static void sortDoctorsByFee() {
        List<Doctor> doctors = doctorService.getDoctorsSortedByFee();
        if (doctors.isEmpty()) {
            System.out.println("No doctors found.");
            return;
        }

        System.out.println("\nDoctors sorted by consultation fee:");
        doctors.forEach(System.out::println);
    }

    // ==========================================================
    // APPOINTMENT MENU
    // ==========================================================

    private static void appointmentMenu() {
        boolean back = false;

        while (!back) {
            System.out.println("\n---------- APPOINTMENT MANAGEMENT ----------");
            System.out.println("1. Create Appointment");
            System.out.println("2. View Appointment");
            System.out.println("3. Confirm Appointment");
            System.out.println("4. Cancel Appointment");
            System.out.println("5. Complete Appointment");
            System.out.println("6. List All Appointments");
            System.out.println("7. View Patient Appointments");
            System.out.println("8. View Doctor Appointments");
            System.out.println("0. Back");

            int choice = readInt("Enter your choice: ");

            try {
                switch (choice) {
                    case 1 -> createAppointment();
                    case 2 -> viewAppointment();
                    case 3 -> confirmAppointment();
                    case 4 -> cancelAppointment();
                    case 5 -> completeAppointment();
                    case 6 -> listAppointments();
                    case 7 -> listPatientAppointments();
                    case 8 -> listDoctorAppointments();
                    case 0 -> back = true;
                    default -> System.out.println("Invalid choice.");
                }

            } catch (InvalidDataException | AppointmentNotFoundException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void createAppointment() {
        int patientId = readInt("Enter patient ID: ");

        Patient patient = patientService.getPatientById(patientId)
                        .orElseThrow(() -> new InvalidDataException("Patient not found: " + patientId));

        int doctorId = readInt("Enter doctor ID: ");
        Doctor doctor = doctorService.getDoctorById(doctorId)
                        .orElseThrow(() -> new InvalidDataException("Doctor not found: " + doctorId));

        String dateTime = readString("Enter appointment date/time " + "(dd-MM-yyyy HH:mm): ");
        LocalDateTime appointmentDateTime = DateUtil.parse(dateTime);

        if (DateUtil.isPast(appointmentDateTime)) {
            throw new InvalidDataException("Appointment date cannot be in the past.");
        }

        Appointment appointment = appointmentService.createAppointment(patient, doctor, appointmentDateTime);

        System.out.println("\nAppointment created successfully.");
        System.out.println(appointment);
    }

    private static void viewAppointment() {
        int id = readInt("Enter appointment ID: ");
        Appointment appointment = appointmentService.getAppointmentById(id);
        System.out.println(appointment);
    }

    private static void confirmAppointment() {
        int id = readInt("Enter appointment ID: ");
        appointmentService.confirmAppointment(id);
        System.out.println("Appointment confirmed successfully.");
    }

    private static void cancelAppointment() {
        int id = readInt("Enter appointment ID: ");
        appointmentService.cancelAppointment(id);
        System.out.println("Appointment cancelled successfully.");
    }

    private static void completeAppointment() {
        int id = readInt("Enter appointment ID: ");
        appointmentService.completeAppointment(id);
        System.out.println("Appointment completed successfully.");
    }

    private static void listAppointments() {
        List<Appointment> appointments = appointmentService.getAllAppointments();
        if (appointments.isEmpty()) {
            System.out.println("No appointments found.");
            return;
        }
        appointments.forEach(System.out::println);
    }

    private static void listPatientAppointments() {
        int patientId = readInt("Enter patient ID: ");
        List<Appointment> appointments = appointmentService.getAppointmentsByPatient(patientId);

        if (appointments.isEmpty()) {
            System.out.println("No appointments found.");
            return;
        }
        appointments.forEach(System.out::println);
    }

    private static void listDoctorAppointments() {
        int doctorId = readInt("Enter doctor ID: ");
        List<Appointment> appointments = appointmentService.getAppointmentsByDoctor(doctorId);

        if (appointments.isEmpty()) {
            System.out.println("No appointments found.");
            return;
        }
        appointments.forEach(System.out::println);
    }

    // ==========================================================
    // BILLING MENU
    // ==========================================================

    private static void billingMenu() {
        boolean back = false;

        while (!back) {
            System.out.println("\n--------------- BILLING ---------------");
            System.out.println("1. Generate Bill");
            System.out.println("0. Back");

            int choice = readInt("Enter your choice: ");
            try {
                switch (choice) {
                    case 1 -> generateBill();
                    case 0 -> back = true;
                    default -> System.out.println("Invalid choice.");
                }
            } catch (InvalidDataException | AppointmentNotFoundException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void generateBill() {
        int appointmentId = readInt("Enter appointment ID: ");
        Appointment appointment = appointmentService.getAppointmentById(appointmentId);
        double fee = appointment.getDoctor().getConsultationFee();

        Bill bill = new Bill(IdGenerator.nextBillId(), appointment.getPatient(), appointment, fee);
        BillSummary summary = bill.generateBill();

        System.out.println("\n============== BILL ==============");
        System.out.println("Bill ID       : " + summary.getBillId());
        System.out.println("Patient       : " + appointment.getPatient().getName());
        System.out.println("Doctor        : " + appointment.getDoctor().getName());
        System.out.println("Subtotal      : ₹" + summary.getSubtotal());
        System.out.println("Tax           : ₹" + summary.getTax());
        System.out.println("Total Amount  : ₹" + summary.getTotal());
        System.out.println("=================================");
    }

    // ==========================================================
    // SEARCH MENU
    // ==========================================================

    private static void searchMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--------------- SEARCH ---------------");
            System.out.println("1. Patient by ID");
            System.out.println("2. Patient by Name");
            System.out.println("3. Patient by Age");
            System.out.println("4. Doctor by ID");
            System.out.println("5. Doctor by Name");
            System.out.println("6. Doctor by Specialization");
            System.out.println("0. Back");

            int choice = readInt("Enter your choice: ");
            try {
                switch (choice) {
                    case 1 -> {
                        int id = readInt("Enter patient ID: ");
                        Patient patient = patientService.getPatientById(id).orElse(null);
                        patient.toString();
                    }
                    case 2 -> searchPatientByName();
                    case 3 -> searchPatientByAge();

                    case 4 -> {
                        int id = readInt("Enter doctor ID: ");
                        Doctor doctor = doctorService.getDoctorById(id).orElse(null);
                        doctor.toString();
                    }
                    case 5 -> searchDoctorByName();
                    case 6 -> searchDoctorBySpecialization();
                    case 0 -> back = true;
                    default -> System.out.println("Invalid choice.");
                }
            } catch (InvalidDataException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    // ==========================================================
    // ANALYTICS MENU
    // ==========================================================

    private static void analyticsMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n-------------- ANALYTICS --------------");
            System.out.println("1. Total Patients");
            System.out.println("2. Total Doctors");
            System.out.println("3. Total Appointments");
            System.out.println("4. Average Doctor Fee");
            System.out.println("5. Doctor Appointments Count");
            System.out.println("6. Doctors Sorted by Fee");
            System.out.println("0. Back");

            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1 -> System.out.println("Total Patients: " + patientService.getAllPatient().size());

                case 2 -> System.out.println("Total Doctors: " + doctorService.getAllDoctors().size());

                case 3 -> System.out.println("Total Appointments: " + appointmentService.getAllAppointments().size());

                case 4 -> System.out.printf("Average Doctor Fee: ₹%.2f%n", doctorService.getAverageConsultationFee());

                case 5 -> showDoctorAppointmentAnalytics();

                case 6 -> doctorService.getDoctorsSortedByFee().forEach(System.out::println);

                case 0 -> back = true;

                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void showDoctorAppointmentAnalytics() {
        List<Appointment> appointments = appointmentService.getAllAppointments();

        if (appointments.isEmpty()) {
            System.out.println("No appointment data available.");
            return;
        }

        appointments.stream()
                .collect(Collectors.groupingBy(appointment -> appointment.getDoctor().getName(), java.util.stream.Collectors.counting()))
                .forEach((doctor, count) -> System.out.println(doctor + " -> " + count + " appointment(s)"));
    }

    // ==========================================================
    // SPECIALIZATION
    // ==========================================================

    private static Specialization selectSpecialization() {
        Specialization[] values = Specialization.values();

        System.out.println("\nSelect specialization:");
        for (int i = 0; i < values.length; i++) {
            System.out.println((i + 1) + ". " + values[i]);
        }

        int choice = readInt("Enter choice: ");
        if (choice < 1 || choice > values.length) {
            throw new InvalidDataException("Invalid specialization choice.");
        }
        return values[choice - 1];
    }

    // ==========================================================
    // INPUT HELPERS
    // ==========================================================

    private static String readString(String message) {

        while (true) {
            System.out.print(message);
            String value = scanner.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("Value cannot be empty.");
        }
    }

    private static int readInt(String message) {
        while (true) {
            try {
                System.out.print(message);
                String value = scanner.nextLine().trim();
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        }
    }

    private static double readDouble(String message) {
        while (true) {
            try {
                System.out.print(message);
                String value = scanner.nextLine().trim();
                double number = Double.parseDouble(value);
                if (number <= 0) {System.out.println("Value must be greater than zero.");
                    continue;
                }
                return number;
            } catch (NumberFormatException e) {
                System.out.println(
                        "Please enter a valid number.");
            }
        }
    }

    // ==========================================================
    // PERSISTENCE PLACEHOLDERS
    // ==========================================================

    private static void saveData() {
        System.out.println("\nData persistence is ready to be connected with CSVUtil.");
        System.out.println("Patient count: " + patientStore.size());
        System.out.println("Doctor count: " + doctorStore.size());
        System.out.println("Appointment count: " + appointmentStore.size());
    }

    private static void loadData() {
        System.out.println("CSV data loading start.");
        try {
            List<String[]> patients = CSVUtil.readCSV(Constants.PATIENT_FILE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
