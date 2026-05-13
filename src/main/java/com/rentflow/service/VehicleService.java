package com.rentflow.service;

import com.rentflow.exception.DuplicateResourceException;
import com.rentflow.exception.InvalidVehicleStateException;
import com.rentflow.exception.ResourceNotFoundException;
import com.rentflow.exception.ValidationException;
import com.rentflow.exception.VehicleNotAvailableException;
import com.rentflow.enums.VehicleStatus;
import com.rentflow.model.Vehicle;
import com.rentflow.repository.VehicleRepository;

import java.time.Year;
import java.util.List;

public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public void addVehicle(Vehicle vehicle) {
        validateVehicle(vehicle);

        boolean plateAlreadyExists = vehicleRepository.findByPlate(vehicle.getPlate()).isPresent();

        if (plateAlreadyExists) {
            throw new DuplicateResourceException("Bu plakaya sahip bir arac zaten var: " + vehicle.getPlate());
        }

        vehicleRepository.save(vehicle);
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public List<Vehicle> getAvailableVehicles() {
        return vehicleRepository.findByStatus(VehicleStatus.AVAILABLE);
    }

    public List<Vehicle> getVehiclesByStatus(VehicleStatus status) {
        return vehicleRepository.findByStatus(status);
    }

    public Vehicle getVehicleByPlate(String plate) {
        return vehicleRepository.findByPlate(plate)
                .orElseThrow(() -> new ResourceNotFoundException("Arac bulunamadi: " + plate));
    }

    public double calculateRentalPrice(String plate, int days) {
        if (days <= 0) {
            throw new ValidationException("Gun sayisi 0'dan buyuk olmalidir.");
        }

        Vehicle vehicle = getVehicleByPlate(plate);

        return vehicle.calculateRentalPrice(days);
    }

    public void rentVehicle(String plate) {
        Vehicle vehicle = getVehicleByPlate(plate);

        if (!vehicle.isAvailable()) {
            throw new VehicleNotAvailableException("Arac kiralanamaz. Arac musait degil: " + plate);
        }

        vehicle.markAsRented();
        vehicleRepository.save(vehicle);
    }

    public void returnVehicle(String plate) {
        Vehicle vehicle = getVehicleByPlate(plate);

        if (vehicle.isAvailable()) {
            throw new InvalidVehicleStateException("Arac zaten musait durumda: " + plate);
        }

        vehicle.markAsAvailable();
        vehicleRepository.save(vehicle);
    }

    private void validateVehicle(Vehicle vehicle) {
        if (vehicle.getPlate() == null || vehicle.getPlate().isBlank()) {
            throw new ValidationException("Plaka bos olamaz.");
        }

        if (vehicle.getBrand() == null || vehicle.getBrand().isBlank()) {
            throw new ValidationException("Marka bos olamaz.");
        }

        if (vehicle.getModel() == null || vehicle.getModel().isBlank()) {
            throw new ValidationException("Model bos olamaz.");
        }

        if (vehicle.getYear() < 1950 || vehicle.getYear() > Year.now().getValue()) {
            throw new ValidationException("Arac yili gecersiz.");
        }

        if (vehicle.getDailyPrice() <= 0) {
            throw new ValidationException("Gunluk fiyat 0'dan buyuk olmalidir.");
        }
    }
}
