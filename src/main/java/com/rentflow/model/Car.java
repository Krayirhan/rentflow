package com.rentflow.model;

import com.rentflow.enums.VehicleStatus;

public class Car extends Vehicle {

    private int seatCount;

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
