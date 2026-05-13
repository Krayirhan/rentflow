package com.rentflow.service;

import com.rentflow.enums.RentalStatus;
import com.rentflow.exception.InvalidRentalDateException;
import com.rentflow.exception.ResourceNotFoundException;
import com.rentflow.exception.VehicleNotAvailableException;
import com.rentflow.model.Customer;
import com.rentflow.model.Rental;
import com.rentflow.model.Vehicle;
import com.rentflow.repository.RentalRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

public class RentalService {

    private static final double LATE_FEE_RATE = 0.20;

    private final RentalRepository rentalRepository;
    private final CustomerService customerService;
    private final VehicleService vehicleService;

    public RentalService(
            RentalRepository rentalRepository,
            CustomerService customerService,
            VehicleService vehicleService
    ) {
        this.rentalRepository = rentalRepository;
        this.customerService = customerService;
        this.vehicleService = vehicleService;
    }

    public Rental rentVehicle(
            String customerId,
            String plate,
            LocalDate startDate,
            LocalDate endDate
    ) {
        validateRentalDates(startDate, endDate);

        Customer customer = customerService.getCustomerById(customerId);
        Vehicle vehicle = vehicleService.getVehicleByPlate(plate);

        if (!vehicle.isAvailable()) {
            throw new VehicleNotAvailableException("Arac musait degil: " + plate);
        }

        int days = calculateDays(startDate, endDate);
        double totalPrice = vehicle.calculateRentalPrice(days);

        Rental rental = new Rental(
                "R-" + UUID.randomUUID(),
                customer,
                vehicle,
                startDate,
                endDate,
                totalPrice
        );

        vehicleService.rentVehicle(plate);
        rentalRepository.save(rental);

        return rental;
    }

    public Rental returnVehicle(String plate) {
        return returnVehicle(plate, LocalDate.now());
    }

    public Rental returnVehicle(String plate, LocalDate actualReturnDate) {
        Rental rental = rentalRepository.findActiveRentalByVehiclePlate(plate)
                .orElseThrow(() -> new ResourceNotFoundException("Bu araca ait aktif kiralama bulunamadi: " + plate));

        if (actualReturnDate == null) {
            throw new InvalidRentalDateException("Gercek iade tarihi bos olamaz.");
        }

        if (actualReturnDate.isBefore(rental.getStartDate())) {
            throw new InvalidRentalDateException("Gercek iade tarihi baslangic tarihinden once olamaz.");
        }

        double lateFee = calculateLateFee(rental, actualReturnDate);
        vehicleService.returnVehicle(plate);
        rental.completeRental(actualReturnDate, lateFee);
        rentalRepository.save(rental);

        return rental;
    }

    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

    public List<Rental> getRentalsByCustomerId(String customerId) {
        customerService.getCustomerById(customerId);
        return rentalRepository.findByCustomerId(customerId);
    }

    public List<Rental> getActiveRentals() {
        return rentalRepository.findByStatus(RentalStatus.ACTIVE);
    }

    public List<Rental> getCompletedRentals() {
        return rentalRepository.findByStatus(RentalStatus.COMPLETED);
    }

    public double getTotalRevenue() {
        return getCompletedRentals().stream()
                .mapToDouble(Rental::getFinalPrice)
                .sum();
    }

    public Rental getRentalById(String rentalId) {
        return rentalRepository.findById(rentalId)
                .orElseThrow(() -> new ResourceNotFoundException("Kiralama kaydi bulunamadi: " + rentalId));
    }

    private void validateRentalDates(LocalDate startDate, LocalDate endDate) {
        if (startDate == null) {
            throw new InvalidRentalDateException("Baslangic tarihi bos olamaz.");
        }

        if (endDate == null) {
            throw new InvalidRentalDateException("Bitis tarihi bos olamaz.");
        }

        if (!endDate.isAfter(startDate)) {
            throw new InvalidRentalDateException("Bitis tarihi baslangic tarihinden sonra olmalidir.");
        }
    }

    private int calculateDays(LocalDate startDate, LocalDate endDate) {
        return (int) ChronoUnit.DAYS.between(startDate, endDate);
    }

    private double calculateLateFee(Rental rental, LocalDate actualReturnDate) {
        if (!actualReturnDate.isAfter(rental.getEndDate())) {
            return 0.0;
        }

        long lateDays = ChronoUnit.DAYS.between(rental.getEndDate(), actualReturnDate);
        return lateDays * rental.getVehicle().getDailyPrice() * LATE_FEE_RATE;
    }
}
