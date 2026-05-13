package com.rentflow.repository.jpa.impl;

import com.rentflow.enums.VehicleStatus;
import com.rentflow.model.Vehicle;
import com.rentflow.repository.VehicleRepository;
import com.rentflow.repository.jpa.JpaVehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@ConditionalOnProperty(name = "rentflow.persistence", havingValue = "database", matchIfMissing = false)
public class JpaVehicleRepositoryImpl implements VehicleRepository {

    @Autowired
    private JpaVehicleRepository jpaVehicleRepository;

    @Override
    public void save(Vehicle vehicle) {
        jpaVehicleRepository.save(vehicle);
    }

    @Override
    public List<Vehicle> findAll() {
        return jpaVehicleRepository.findAll();
    }

    @Override
    public Optional<Vehicle> findByPlate(String plate) {
        return jpaVehicleRepository.findById(plate);
    }

    @Override
    public List<Vehicle> findByStatus(VehicleStatus status) {
        return jpaVehicleRepository.findByStatus(status);
    }
}
