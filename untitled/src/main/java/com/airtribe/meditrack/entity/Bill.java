package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.constants.Constants;
import com.airtribe.meditrack.exception.InvalidDataException;
import com.airtribe.meditrack.interfaces.Payable;

import java.lang.constant.Constable;

public class Bill implements Payable {

    private final int billId;
    private final Patient patient;
    private final double consultationFee;
    private Appointment appointment;

    public Bill(int billId, Patient patient, double consultationFee) {
        this.billId = billId;
        this.patient = patient;
        this.consultationFee = consultationFee;
    }

    public Bill(int billId, Patient patient,Appointment appointment, double consultationFee) {
        this.billId = billId;
        this.patient = patient;
        this.appointment = appointment;
        if(consultationFee <=0) throw new InvalidDataException("Consultant Fee should be greater than 0");
        this.consultationFee = consultationFee;
    }

    @Override
    public double calculateAmount() {
        return consultationFee;
    }

    public BillSummary generateBill() {
        double tax = Constants.TAX_RATE * consultationFee;
        double total =consultationFee + tax;
        return new BillSummary(this.billId, this.consultationFee, tax, total);
    }
}