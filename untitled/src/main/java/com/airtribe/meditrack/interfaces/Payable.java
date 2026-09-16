package com.airtribe.meditrack.interfaces;

public interface Payable {

    double calculateAmount();

    default double calculateTax(double amount, double taxRate) {
        return amount * taxRate;
    }
}