package com.airtribe.meditrack.entity;

public final class BillSummary {

    private final int billId;
    private final double subtotal;
    private final double tax;
    private final double total;

    public BillSummary(int billId, double subtotal, double tax, double total) {
        this.billId = billId;
        this.subtotal = subtotal;
        this.tax = tax;
        this.total = total;
    }

    public int getBillId() {
        return billId;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getTax() {
        return tax;
    }

    public double getTotal() {
        return total;
    }

    @Override
    public String toString() {
        return "BillSummary{" +
                "billId=" + billId +
                ", subtotal=" + subtotal +
                ", tax=" + tax +
                ", total=" + total +
                '}';
    }
}