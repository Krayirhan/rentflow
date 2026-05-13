package com.rentflow.model;

import com.rentflow.enums.VehicleStatus;

public abstract class Vehicle {

    private String plate;
    private String brand;
    private String model;
    private int year;
    private double dailyPrice;
    private VehicleStatus status;

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
