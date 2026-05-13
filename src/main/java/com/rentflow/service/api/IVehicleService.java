package com.rentflow.service.api;

import com.rentflow.model.Vehicle;

import java.util.List;

public interface IVehicleService {

    void addVehicle(Vehicle vehicle);

    List<Vehicle> getAllVehicles();

    Vehicle getVehicleByPlate(String plate);

    double calculateRentalPrice(String plate, int days);

    void rentVehicle(String plate);

    void returnVehicle(String plate);
}
