package com.rentflow.model;

import com.rentflow.enums.RentalStatus;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "rentals")
public class Rental {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicle_plate", nullable = false)
    private Vehicle vehicle;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private double totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RentalStatus status;

    private LocalDate actualReturnDate;

    private double lateFee;

    private double finalPrice;

    public Rental() {
    }

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
