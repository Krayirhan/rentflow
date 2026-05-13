package com.rentflow.repository.memory;

import com.rentflow.enums.VehicleStatus;
import com.rentflow.model.Vehicle;
import com.rentflow.repository.VehicleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InMemoryVehicleRepository implements VehicleRepository {

    private final List<Vehicle> vehicles = new ArrayList<>();

    @Override
    public void save(Vehicle vehicle) {
        findByPlate(vehicle.getPlate()).ifPresent(vehicles::remove);
        vehicles.add(vehicle);
    }

    @Override
    public List<Vehicle> findAll() {
        return new ArrayList<>(vehicles);
    }

    @Override
    public Optional<Vehicle> findByPlate(String plate) {
        return vehicles.stream()
                .filter(vehicle -> vehicle.getPlate().equalsIgnoreCase(plate))
                .findFirst();
    }

    @Override
    public List<Vehicle> findByStatus(VehicleStatus status) {
        return vehicles.stream()
                .filter(vehicle -> vehicle.getStatus() == status)
                .collect(Collectors.toList());
    }
}
