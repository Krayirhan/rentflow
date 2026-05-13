package com.rentflow.api.controller;

import com.rentflow.model.Vehicle;
import com.rentflow.service.VehicleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehicles")
public class VehicleRestController {

    private final VehicleService vehicleService;

    public VehicleRestController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping
    public ResponseEntity<List<Vehicle>> getAllVehicles() {
        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        return ResponseEntity.ok(vehicles);
    }

    @GetMapping("/{plate}")
    public ResponseEntity<Vehicle> getVehicleByPlate(@PathVariable String plate) {
        Vehicle vehicle = vehicleService.getVehicleByPlate(plate);
        return ResponseEntity.ok(vehicle);
    }

    @PostMapping
    public ResponseEntity<String> addVehicle(@RequestBody Vehicle vehicle) {
        vehicleService.addVehicle(vehicle);
        return ResponseEntity.status(HttpStatus.CREATED).body("Araç başarıyla eklendi");
    }

    @GetMapping("/{plate}/price")
    public ResponseEntity<Double> calculateRentalPrice(
            @PathVariable String plate,
            @RequestParam int days
    ) {
        double price = vehicleService.calculateRentalPrice(plate, days);
        return ResponseEntity.ok(price);
    }

    @PutMapping("/{plate}/rent")
    public ResponseEntity<String> rentVehicle(@PathVariable String plate) {
        vehicleService.rentVehicle(plate);
        return ResponseEntity.ok("Araç kiraya verildi");
    }

    @PutMapping("/{plate}/return")
    public ResponseEntity<String> returnVehicle(@PathVariable String plate) {
        vehicleService.returnVehicle(plate);
        return ResponseEntity.ok("Araç iade alındı");
    }
}
