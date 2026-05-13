package com.rentflow.model;

import com.rentflow.enums.VehicleStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "cars")
public class Car extends Vehicle {

    @Column(nullable = false)
    private int seatCount;

    public Car() {
    }

    public Car(String plate, String brand, String model, int year, double dailyPrice, int seatCount) {
        this(plate, brand, model, year, dailyPrice, seatCount, VehicleStatus.AVAILABLE);
    }

    public Car(
            String plate,
            String brand,
            String model,
            int year,
            double dailyPrice,
            int seatCount,
            VehicleStatus status
    ) {
        super(plate, brand, model, year, dailyPrice, status);
        this.seatCount = seatCount;
    }

    @Override
    public double calculateRentalPrice(int days) {
        return getDailyPrice() * days;
    }

    public int getSeatCount() {
        return seatCount;
    }
}
