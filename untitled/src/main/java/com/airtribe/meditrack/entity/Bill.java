package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.interfaces.Payable;

public class Bill implements Payable {

    private final int billId;
    private final Patient patient;
    private final double consultationFee;

    public Bill(int billId, Patient patient, double consultationFee) {
        this.billId = billId;
        this.patient = patient;
        this.consultationFee = consultationFee;
    }

    @Override
    public double calculateAmount() {
        return consultationFee;
    }
}