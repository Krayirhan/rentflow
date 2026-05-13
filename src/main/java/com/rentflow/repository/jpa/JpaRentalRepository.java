package com.rentflow.repository.jpa;

import com.rentflow.enums.RentalStatus;
import com.rentflow.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaRentalRepository extends JpaRepository<Rental, String> {
    List<Rental> findByCustomerId(String customerId);

    Optional<Rental> findByVehiclePlateAndStatus(String vehiclePlate, RentalStatus status);

    List<Rental> findByStatus(RentalStatus status);
}
