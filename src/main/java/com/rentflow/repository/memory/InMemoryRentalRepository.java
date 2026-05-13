package com.rentflow.repository.memory;

import com.rentflow.enums.RentalStatus;
import com.rentflow.model.Rental;
import com.rentflow.repository.RentalRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InMemoryRentalRepository implements RentalRepository {

    private final List<Rental> rentals = new ArrayList<>();

    @Override
    public void save(Rental rental) {
        findById(rental.getId()).ifPresent(rentals::remove);
        rentals.add(rental);
    }

    @Override
    public List<Rental> findAll() {
        return new ArrayList<>(rentals);
    }

    @Override
    public Optional<Rental> findById(String id) {
        return rentals.stream()
                .filter(rental -> rental.getId().equalsIgnoreCase(id))
                .findFirst();
    }

    @Override
    public List<Rental> findByCustomerId(String customerId) {
        return rentals.stream()
                .filter(rental -> rental.getCustomer().getId().equalsIgnoreCase(customerId))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Rental> findActiveRentalByVehiclePlate(String plate) {
        return rentals.stream()
                .filter(rental -> rental.getVehicle().getPlate().equalsIgnoreCase(plate))
                .filter(rental -> rental.getStatus() == RentalStatus.ACTIVE)
                .findFirst();
    }

    @Override
    public List<Rental> findByStatus(RentalStatus status) {
        return rentals.stream()
                .filter(rental -> rental.getStatus() == status)
                .collect(Collectors.toList());
    }
}
