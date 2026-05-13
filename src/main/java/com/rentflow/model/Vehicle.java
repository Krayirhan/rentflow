package com.rentflow.model;

import com.rentflow.enums.VehicleStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "vehicles")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "vehicle_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Vehicle {

    @Id
    private String plate;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false)
    private double dailyPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleStatus status;

    public Vehicle() {
    }

    public Vehicle(String plate, String brand, String model, int year, double dailyPrice) {
        this(plate, brand, model, year, dailyPrice, VehicleStatus.AVAILABLE);
    }

    public Vehicle(
            String plate,
            String brand,
            String model,
            int year,
            double dailyPrice,
            VehicleStatus status
    ) {
        this.plate = plate;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.dailyPrice = dailyPrice;
        this.status = status;
    }

    public abstract double calculateRentalPrice(int days);

    public boolean isAvailable() {
        return this.status == VehicleStatus.AVAILABLE;
    }

    public void markAsRented() {
        this.status = VehicleStatus.RENTED;
    }

    public void markAsAvailable() {
        this.status = VehicleStatus.AVAILABLE;
    }

    public void updateStatus(VehicleStatus status) {
        this.status = status;
    }

    public String getPlate() {
        return plate;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }

    public double getDailyPrice() {
        return dailyPrice;
    }

    public VehicleStatus getStatus() {
        return status;
    }
}
