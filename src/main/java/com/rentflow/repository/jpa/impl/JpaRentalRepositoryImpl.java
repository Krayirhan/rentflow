package com.rentflow.repository.jpa.impl;

import com.rentflow.enums.RentalStatus;
import com.rentflow.model.Rental;
import com.rentflow.repository.RentalRepository;
import com.rentflow.repository.jpa.JpaRentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@ConditionalOnProperty(name = "rentflow.persistence", havingValue = "database", matchIfMissing = false)
public class JpaRentalRepositoryImpl implements RentalRepository {

    @Autowired
    private JpaRentalRepository jpaRentalRepository;

    @Override
    public void save(Rental rental) {
        jpaRentalRepository.save(rental);
    }

    @Override
    public List<Rental> findAll() {
        return jpaRentalRepository.findAll();
    }

    @Override
    public Optional<Rental> findById(String id) {
        return jpaRentalRepository.findById(id);
    }

    @Override
    public List<Rental> findByCustomerId(String customerId) {
        return jpaRentalRepository.findByCustomerId(customerId);
    }

    @Override
    public Optional<Rental> findActiveRentalByVehiclePlate(String vehiclePlate) {
        return jpaRentalRepository.findByVehiclePlateAndStatus(vehiclePlate, RentalStatus.ACTIVE);
    }

    @Override
    public List<Rental> findByStatus(RentalStatus status) {
        return jpaRentalRepository.findByStatus(status);
    }
}
