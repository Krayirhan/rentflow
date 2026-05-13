package com.rentflow;

import com.rentflow.exception.InvalidRentalDateException;
import com.rentflow.exception.ValidationException;
import com.rentflow.model.Car;
import com.rentflow.model.Customer;
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
import java.time.Year;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RentFlowValidationTest {

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
        customerService.addCustomer(new Customer("C-1", "Ahmet Yilmaz", "05551234567"));
    }

    @Test
    void shouldRejectBlankVehiclePlate() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> vehicleService.addVehicle(new Car("", "Toyota", "Corolla", 2020, 1500, 5))
        );

        assertEquals("Plaka bos olamaz.", exception.getMessage());
    }

    @Test
    void shouldRejectBlankVehicleBrand() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> vehicleService.addVehicle(new Car("35XYZ789", "", "Corolla", 2020, 1500, 5))
        );

        assertEquals("Marka bos olamaz.", exception.getMessage());
    }

    @Test
    void shouldRejectBlankVehicleModel() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> vehicleService.addVehicle(new Car("35XYZ789", "Toyota", "", 2020, 1500, 5))
        );

        assertEquals("Model bos olamaz.", exception.getMessage());
    }

    @Test
    void shouldRejectVehicleYearBefore1950() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> vehicleService.addVehicle(new Car("35XYZ789", "Toyota", "Corolla", 1949, 1500, 5))
        );

        assertEquals("Arac yili gecersiz.", exception.getMessage());
    }

    @Test
    void shouldRejectFutureVehicleYear() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> vehicleService.addVehicle(new Car("35XYZ789", "Toyota", "Corolla", Year.now().getValue() + 1, 1500, 5))
        );

        assertEquals("Arac yili gecersiz.", exception.getMessage());
    }

    @Test
    void shouldRejectZeroVehicleDailyPrice() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> vehicleService.addVehicle(new Car("35XYZ789", "Toyota", "Corolla", 2020, 0, 5))
        );

        assertEquals("Gunluk fiyat 0'dan buyuk olmalidir.", exception.getMessage());
    }

    @Test
    void shouldRejectNegativeVehicleDailyPrice() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> vehicleService.addVehicle(new Car("35XYZ789", "Toyota", "Corolla", 2020, -100, 5))
        );

        assertEquals("Gunluk fiyat 0'dan buyuk olmalidir.", exception.getMessage());
    }

    @Test
    void shouldRejectBlankCustomerId() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> customerService.addCustomer(new Customer("", "Mehmet Kaya", "05550001122"))
        );

        assertEquals("Musteri id bos olamaz.", exception.getMessage());
    }

    @Test
    void shouldRejectBlankCustomerFullName() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> customerService.addCustomer(new Customer("C-2", "", "05550001122"))
        );

        assertEquals("Musteri adi bos olamaz.", exception.getMessage());
    }

    @Test
    void shouldRejectBlankCustomerPhoneNumber() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> customerService.addCustomer(new Customer("C-2", "Mehmet Kaya", ""))
        );

        assertEquals("Telefon numarasi bos olamaz.", exception.getMessage());
    }

    @Test
    void shouldRejectNullRentalStartDate() {
        InvalidRentalDateException exception = assertThrows(
                InvalidRentalDateException.class,
                () -> rentalService.rentVehicle("C-1", "34ABC123", null, LocalDate.of(2026, 5, 13))
        );

        assertEquals("Baslangic tarihi bos olamaz.", exception.getMessage());
    }

    @Test
    void shouldRejectNullRentalEndDate() {
        InvalidRentalDateException exception = assertThrows(
                InvalidRentalDateException.class,
                () -> rentalService.rentVehicle("C-1", "34ABC123", LocalDate.of(2026, 5, 10), null)
        );

        assertEquals("Bitis tarihi bos olamaz.", exception.getMessage());
    }

    @Test
    void shouldRejectRentalEndDateBeforeStartDate() {
        InvalidRentalDateException exception = assertThrows(
                InvalidRentalDateException.class,
                () -> rentalService.rentVehicle("C-1", "34ABC123", LocalDate.of(2026, 5, 13), LocalDate.of(2026, 5, 10))
        );

        assertEquals("Bitis tarihi baslangic tarihinden sonra olmalidir.", exception.getMessage());
    }

    @Test
    void shouldRejectRentalEndDateEqualToStartDate() {
        InvalidRentalDateException exception = assertThrows(
                InvalidRentalDateException.class,
                () -> rentalService.rentVehicle("C-1", "34ABC123", LocalDate.of(2026, 5, 10), LocalDate.of(2026, 5, 10))
        );

        assertEquals("Bitis tarihi baslangic tarihinden sonra olmalidir.", exception.getMessage());
    }

    @Test
    void shouldRejectActualReturnDateBeforeRentalStartDate() {
        rentalService.rentVehicle("C-1", "34ABC123", LocalDate.of(2026, 5, 10), LocalDate.of(2026, 5, 13));

        InvalidRentalDateException exception = assertThrows(
                InvalidRentalDateException.class,
                () -> rentalService.returnVehicle("34ABC123", LocalDate.of(2026, 5, 9))
        );

        assertEquals("Gercek iade tarihi baslangic tarihinden once olamaz.", exception.getMessage());
    }
}
