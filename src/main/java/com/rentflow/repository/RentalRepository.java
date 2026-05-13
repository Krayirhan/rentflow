package com.rentflow.repository;

import com.rentflow.enums.RentalStatus;
import com.rentflow.model.Rental;

import java.util.List;
import java.util.Optional;

public interface RentalRepository {

    void save(Rental rental);

    List<Rental> findAll();

    Optional<Rental> findById(String id);

    List<Rental> findByCustomerId(String customerId);

    Optional<Rental> findActiveRentalByVehiclePlate(String plate);

    List<Rental> findByStatus(RentalStatus status);
}
