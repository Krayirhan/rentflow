package com.rentflow.repository;

import com.rentflow.enums.VehicleStatus;
import com.rentflow.model.Vehicle;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository {

    void save(Vehicle vehicle);

    List<Vehicle> findAll();

    Optional<Vehicle> findByPlate(String plate);

    List<Vehicle> findByStatus(VehicleStatus status);
}
