package com.rentflow;

import com.rentflow.model.Car;
import com.rentflow.model.Customer;
import com.rentflow.model.Rental;
import com.rentflow.persistence.CsvFileReader;
import com.rentflow.persistence.CsvFileWriter;
import com.rentflow.persistence.FilePathProvider;
import com.rentflow.repository.CustomerRepository;
import com.rentflow.repository.RentalRepository;
import com.rentflow.repository.VehicleRepository;
import com.rentflow.repository.file.CsvCustomerRepository;
import com.rentflow.repository.file.CsvRentalRepository;
import com.rentflow.repository.file.CsvVehicleRepository;
import com.rentflow.service.CustomerService;
import com.rentflow.service.RentalService;
import com.rentflow.service.VehicleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RentFlowCsvPersistenceTest {

    @TempDir
    Path tempDir;

    @Test
    void csvRepositoriesShouldPersistAndReloadVehiclesCustomersAndRentals() {
        FilePathProvider filePathProvider = new FilePathProvider(tempDir);
        CsvFileReader csvFileReader = new CsvFileReader();
        CsvFileWriter csvFileWriter = new CsvFileWriter();

        VehicleRepository vehicleRepository = new CsvVehicleRepository(
                filePathProvider.getVehiclesFilePath(),
                csvFileReader,
                csvFileWriter
        );
        CustomerRepository customerRepository = new CsvCustomerRepository(
                filePathProvider.getCustomersFilePath(),
                csvFileReader,
                csvFileWriter
        );
        RentalRepository rentalRepository = new CsvRentalRepository(
                filePathProvider.getRentalsFilePath(),
                csvFileReader,
                csvFileWriter,
                customerRepository,
                vehicleRepository
        );

        VehicleService vehicleService = new VehicleService(vehicleRepository);
        CustomerService customerService = new CustomerService(customerRepository);
        RentalService rentalService = new RentalService(rentalRepository, customerService, vehicleService);

        vehicleService.addVehicle(new Car("34CSV34", "Toyota", "Yaris", 2021, 1000, 5));
        customerService.addCustomer(new Customer("C-CSV", "Csv Tester", "05000000000"));

        Rental createdRental = rentalService.rentVehicle(
                "C-CSV",
                "34CSV34",
                LocalDate.of(2026, 5, 10),
                LocalDate.of(2026, 5, 12)
        );
        rentalService.returnVehicle("34CSV34", LocalDate.of(2026, 5, 13));

        VehicleRepository reloadedVehicleRepository = new CsvVehicleRepository(
                filePathProvider.getVehiclesFilePath(),
                csvFileReader,
                csvFileWriter
        );
        CustomerRepository reloadedCustomerRepository = new CsvCustomerRepository(
                filePathProvider.getCustomersFilePath(),
                csvFileReader,
                csvFileWriter
        );
        RentalRepository reloadedRentalRepository = new CsvRentalRepository(
                filePathProvider.getRentalsFilePath(),
                csvFileReader,
                csvFileWriter,
                reloadedCustomerRepository,
                reloadedVehicleRepository
        );

        Rental reloadedRental = reloadedRentalRepository.findById(createdRental.getId()).orElseThrow();

        assertEquals(1, reloadedVehicleRepository.findAll().size());
        assertEquals(1, reloadedCustomerRepository.findAll().size());
        assertEquals(1, reloadedRentalRepository.findAll().size());
        assertEquals("C-CSV", reloadedRental.getCustomer().getId());
        assertEquals("34CSV34", reloadedRental.getVehicle().getPlate());
        assertEquals(LocalDate.of(2026, 5, 13), reloadedRental.getActualReturnDate());
        assertEquals(200.0, reloadedRental.getLateFee());
        assertEquals(2200.0, reloadedRental.getFinalPrice());
    }
}
