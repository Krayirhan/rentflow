package com.rentflow.model;

import com.rentflow.enums.RentalStatus;

import java.time.LocalDate;

public class Rental {

    private String id;
    private Customer customer;
    private Vehicle vehicle;
    private LocalDate startDate;
    private LocalDate endDate;
    private double totalPrice;
    private RentalStatus status;
    private LocalDate actualReturnDate;
    private double lateFee;
    private double finalPrice;

    public Rental(
            String id,
            Customer customer,
            Vehicle vehicle,
            LocalDate startDate,
            LocalDate endDate,
            double totalPrice
    ) {
        this(id, customer, vehicle, startDate, endDate, totalPrice, RentalStatus.ACTIVE, null, 0.0, totalPrice);
    }

    public Rental(
            String id,
            Customer customer,
            Vehicle vehicle,
            LocalDate startDate,
            LocalDate endDate,
            double totalPrice,
            RentalStatus status,
            LocalDate actualReturnDate,
            double lateFee,
            double finalPrice
    ) {
        this.id = id;
        this.customer = customer;
        this.vehicle = vehicle;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalPrice = totalPrice;
        this.status = status;
        this.actualReturnDate = actualReturnDate;
        this.lateFee = lateFee;
        this.finalPrice = finalPrice;
    }

    public void completeRental(LocalDate actualReturnDate, double lateFee) {
        this.status = RentalStatus.COMPLETED;
        this.actualReturnDate = actualReturnDate;
        this.lateFee = lateFee;
        this.finalPrice = totalPrice + lateFee;
    }

    public void cancelRental() {
        this.status = RentalStatus.CANCELLED;
    }

    public String getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public RentalStatus getStatus() {
        return status;
    }

    public LocalDate getActualReturnDate() {
        return actualReturnDate;
    }

    public double getLateFee() {
        return lateFee;
    }

    public double getFinalPrice() {
        return finalPrice;
    }
}
