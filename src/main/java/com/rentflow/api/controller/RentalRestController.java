package com.rentflow.api.controller;

import com.rentflow.model.Rental;
import com.rentflow.service.RentalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/rentals")
public class RentalRestController {

    private final RentalService rentalService;

    public RentalRestController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @GetMapping
    public ResponseEntity<List<Rental>> getAllRentals() {
        List<Rental> rentals = rentalService.getAllRentals();
        return ResponseEntity.ok(rentals);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rental> getRentalById(@PathVariable String id) {
        Rental rental = rentalService.getRentalById(id);
        return ResponseEntity.ok(rental);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Rental>> getRentalsByCustomerId(@PathVariable String customerId) {
        List<Rental> rentals = rentalService.getRentalsByCustomerId(customerId);
        return ResponseEntity.ok(rentals);
    }

    @PostMapping
    public ResponseEntity<Rental> rentVehicle(
            @RequestParam String customerId,
            @RequestParam String plate,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {
        Rental rental = rentalService.rentVehicle(customerId, plate, startDate, endDate);
        return ResponseEntity.status(HttpStatus.CREATED).body(rental);
    }

    @PutMapping("/{plate}/return")
    public ResponseEntity<Rental> returnVehicle(@PathVariable String plate) {
        Rental rental = rentalService.returnVehicle(plate);
        return ResponseEntity.ok(rental);
    }
}
