package com.rentflow.repository.jpa;

import com.rentflow.enums.VehicleStatus;
import com.rentflow.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaVehicleRepository extends JpaRepository<Vehicle, String> {
    List<Vehicle> findByStatus(VehicleStatus status);
}
