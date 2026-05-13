package com.rentflow.model;

import com.rentflow.enums.VehicleStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "motorcycles")
public class Motorcycle extends Vehicle {

    @Column(nullable = false)
    private boolean helmetIncluded;

    public Motorcycle() {
    }

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
