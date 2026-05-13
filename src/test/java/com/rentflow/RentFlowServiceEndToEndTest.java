package com.rentflow;

import com.rentflow.enums.RentalStatus;
import com.rentflow.enums.VehicleStatus;
import com.rentflow.exception.DuplicateResourceException;
import com.rentflow.exception.InvalidRentalDateException;
import com.rentflow.exception.InvalidVehicleStateException;
import com.rentflow.exception.ResourceNotFoundException;
import com.rentflow.exception.VehicleNotAvailableException;
import com.rentflow.model.Car;
import com.rentflow.model.Customer;
import com.rentflow.model.Motorcycle;
import com.rentflow.model.Rental;
import com.rentflow.repository.CustomerRepository;
import com.rentflow.repository.RentalRepository;
import com.rentflow.repository.VehicleRepository;
import com.rentflow.repository.memory.InMemoryCustomerRepository;
import com.rentflow.repository.memory.InMemoryRentalRepository;
import com.rentflow.repository.memory.InMemoryVehicleRepository;
import com.rentflow.service.CustomerService;
import com.rentflow.service.RentalService;
import com.rentflow.service.VehicleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RentFlowServiceEndToEndTest {

    private VehicleService vehicleService;
    private CustomerService customerService;
    private RentalService rentalService;

    @BeforeEach
    void setUp() {
        VehicleRepository vehicleRepository = new InMemoryVehicleRepository();
        CustomerRepository customerRepository = new InMemoryCustomerRepository();
        RentalRepository rentalRepository = new InMemoryRentalRepository();

        vehicleService = new VehicleService(vehicleRepository);
        customerService = new CustomerService(customerRepository);
        rentalService = new RentalService(rentalRepository, customerService, vehicleService);

        vehicleService.addVehicle(new Car("34ABC123", "Toyota", "Corolla", 2020, 1500, 5));
        vehicleService.addVehicle(new Motorcycle("34MOTO34", "Yamaha", "MT-07", 2022, 900, true));

        customerService.addCustomer(new Customer("C-1", "Ahmet Yilmaz", "05551234567"));
        customerService.addCustomer(new Customer("C-2", "Ayse Demir", "05559876543"));
    }

    @Test
    void carRentalFlowShouldUpdateRentalAndVehicleStatusesEndToEnd() {
        Rental rental = rentalService.rentVehicle(
                "C-1",
                "34ABC123",
                LocalDate.of(2026, 5, 10),
                LocalDate.of(2026, 5, 13)
        );

        assertNotNull(rental.getId());
        assertEquals(RentalStatus.ACTIVE, rental.getStatus());
        assertEquals(4500.0, rental.getTotalPrice());
        assertEquals(4500.0, rental.getFinalPrice());
        assertEquals(VehicleStatus.RENTED, vehicleService.getVehicleByPlate("34ABC123").getStatus());

        VehicleNotAvailableException rentAgainException = assertThrows(
                VehicleNotAvailableException.class,
                () -> rentalService.rentVehicle(
                        "C-2",
                        "34ABC123",
                        LocalDate.of(2026, 5, 14),
                        LocalDate.of(2026, 5, 16)
                )
        );
        assertEquals("Arac musait degil: 34ABC123", rentAgainException.getMessage());

        Rental returnedRental = rentalService.returnVehicle("34ABC123", LocalDate.of(2026, 5, 15));

        assertEquals(rental.getId(), returnedRental.getId());
        assertEquals(RentalStatus.COMPLETED, returnedRental.getStatus());
        assertEquals(LocalDate.of(2026, 5, 15), returnedRental.getActualReturnDate());
        assertEquals(600.0, returnedRental.getLateFee());
        assertEquals(5100.0, returnedRental.getFinalPrice());
        assertEquals(VehicleStatus.AVAILABLE, vehicleService.getVehicleByPlate("34ABC123").getStatus());

        ResourceNotFoundException returnAgainException = assertThrows(
                ResourceNotFoundException.class,
                () -> rentalService.returnVehicle("34ABC123", LocalDate.of(2026, 5, 15))
        );
        assertEquals("Bu araca ait aktif kiralama bulunamadi: 34ABC123", returnAgainException.getMessage());
    }

    @Test
    void motorcycleHelmetIncludedPriceShouldBeCalculatedCorrectly() {
        Rental rental = rentalService.rentVehicle(
                "C-1",
                "34MOTO34",
                LocalDate.of(2026, 6, 1),
                LocalDate.of(2026, 6, 4)
        );

        assertEquals(2850.0, rental.getTotalPrice());
        assertEquals(RentalStatus.ACTIVE, rental.getStatus());
        assertEquals(VehicleStatus.RENTED, vehicleService.getVehicleByPlate("34MOTO34").getStatus());
    }

    @Test
    void duplicateVehiclePlateShouldBeRejected() {
        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> vehicleService.addVehicle(new Car("34ABC123", "Ford", "Focus", 2021, 1300, 5))
        );

        assertEquals("Bu plakaya sahip bir arac zaten var: 34ABC123", exception.getMessage());
    }

    @Test
    void duplicateCustomerIdShouldBeRejected() {
        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> customerService.addCustomer(new Customer("C-1", "Mehmet Kaya", "05550000000"))
        );

        assertEquals("Bu id'ye sahip bir musteri zaten var: C-1", exception.getMessage());
    }

    @Test
    void invalidRentalDatesShouldBeRejected() {
        InvalidRentalDateException sameDayException = assertThrows(
                InvalidRentalDateException.class,
                () -> rentalService.rentVehicle(
                        "C-1",
                        "34ABC123",
                        LocalDate.of(2026, 5, 10),
                        LocalDate.of(2026, 5, 10)
                )
        );
        assertEquals("Bitis tarihi baslangic tarihinden sonra olmalidir.", sameDayException.getMessage());

        InvalidRentalDateException reversedDatesException = assertThrows(
                InvalidRentalDateException.class,
                () -> rentalService.rentVehicle(
                        "C-1",
                        "34ABC123",
                        LocalDate.of(2026, 5, 12),
                        LocalDate.of(2026, 5, 10)
                )
        );
        assertEquals("Bitis tarihi baslangic tarihinden sonra olmalidir.", reversedDatesException.getMessage());
    }

    @Test
    void unknownCustomerShouldBeRejected() {
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> rentalService.rentVehicle(
                        "C-999",
                        "34ABC123",
                        LocalDate.of(2026, 5, 10),
                        LocalDate.of(2026, 5, 13)
                )
        );

        assertEquals("Musteri bulunamadi: C-999", exception.getMessage());
    }

    @Test
    void unknownVehicleShouldBeRejected() {
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> rentalService.rentVehicle(
                        "C-1",
                        "00XXX00",
                        LocalDate.of(2026, 5, 10),
                        LocalDate.of(2026, 5, 13)
                )
        );

        assertEquals("Arac bulunamadi: 00XXX00", exception.getMessage());
    }

    @Test
    void returningAvailableVehicleShouldBeRejected() {
        InvalidVehicleStateException exception = assertThrows(
                InvalidVehicleStateException.class,
                () -> vehicleService.returnVehicle("34ABC123")
        );

        assertEquals("Arac zaten musait durumda: 34ABC123", exception.getMessage());
    }

    @Test
    void reportsShouldReflectActiveCompletedAndRevenueTotals() {
        rentalService.rentVehicle("C-1", "34ABC123", LocalDate.of(2026, 5, 10), LocalDate.of(2026, 5, 13));
        rentalService.rentVehicle("C-2", "34MOTO34", LocalDate.of(2026, 5, 11), LocalDate.of(2026, 5, 13));

        assertEquals(2, rentalService.getActiveRentals().size());
        assertEquals(0, rentalService.getCompletedRentals().size());
        assertEquals(0.0, rentalService.getTotalRevenue());
        assertEquals(0, vehicleService.getAvailableVehicles().size());

        rentalService.returnVehicle("34ABC123", LocalDate.of(2026, 5, 14));

        assertEquals(1, rentalService.getActiveRentals().size());
        assertEquals(1, rentalService.getCompletedRentals().size());
        assertEquals(4800.0, rentalService.getTotalRevenue());
        assertEquals(1, vehicleService.getAvailableVehicles().size());
    }
}
