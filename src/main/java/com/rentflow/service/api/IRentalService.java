package com.rentflow.service.api;

import com.rentflow.model.Rental;

import java.time.LocalDate;
import java.util.List;

public interface IRentalService {

    Rental rentVehicle(String customerId, String plate, LocalDate startDate, LocalDate endDate);

    Rental returnVehicle(String plate);

    List<Rental> getAllRentals();

    List<Rental> getRentalsByCustomerId(String customerId);

    Rental getRentalById(String rentalId);
}
