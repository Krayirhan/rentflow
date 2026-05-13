package com.rentflow.model;

import com.rentflow.enums.VehicleStatus;

public class Motorcycle extends Vehicle {

    private boolean helmetIncluded;

    public Motorcycle(String plate, String brand, String model, int year, double dailyPrice, boolean helmetIncluded) {
        this(plate, brand, model, year, dailyPrice, helmetIncluded, VehicleStatus.AVAILABLE);
    }

    public Motorcycle(
            String plate,
            String brand,
            String model,
            int year,
            double dailyPrice,
            boolean helmetIncluded,
            VehicleStatus status
    ) {
        super(plate, brand, model, year, dailyPrice, status);
        this.helmetIncluded = helmetIncluded;
    }

    @Override
    public double calculateRentalPrice(int days) {
        if (helmetIncluded) {
            return getDailyPrice() * days + (50 * days);
        }

        return getDailyPrice() * days;
    }

    public boolean isHelmetIncluded() {
        return helmetIncluded;
    }
}
