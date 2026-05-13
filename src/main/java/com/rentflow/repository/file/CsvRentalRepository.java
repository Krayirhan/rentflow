package com.rentflow.repository.file;

import com.rentflow.enums.RentalStatus;
import com.rentflow.model.Customer;
import com.rentflow.model.Rental;
import com.rentflow.model.Vehicle;
import com.rentflow.persistence.CsvFileReader;
import com.rentflow.persistence.CsvFileWriter;
import com.rentflow.repository.CustomerRepository;
import com.rentflow.repository.RentalRepository;
import com.rentflow.repository.VehicleRepository;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CsvRentalRepository implements RentalRepository {

    private static final List<String> HEADER = Arrays.asList(
            "id",
            "customerId",
            "vehiclePlate",
            "startDate",
            "endDate",
            "totalPrice",
            "status",
            "actualReturnDate",
            "lateFee",
            "finalPrice"
    );

    private final Path filePath;
    private final CsvFileReader csvFileReader;
    private final CsvFileWriter csvFileWriter;
    private final CustomerRepository customerRepository;
    private final VehicleRepository vehicleRepository;

    public CsvRentalRepository(
            Path filePath,
            CsvFileReader csvFileReader,
            CsvFileWriter csvFileWriter,
            CustomerRepository customerRepository,
            VehicleRepository vehicleRepository
    ) {
        this.filePath = filePath;
        this.csvFileReader = csvFileReader;
        this.csvFileWriter = csvFileWriter;
        this.customerRepository = customerRepository;
        this.vehicleRepository = vehicleRepository;
        initializeFile();
    }

    @Override
    public void save(Rental rental) {
        List<Rental> rentals = findAll();
        rentals.removeIf(existingRental -> existingRental.getId().equalsIgnoreCase(rental.getId()));
        rentals.add(rental);
        writeRentals(rentals);
    }

    @Override
    public List<Rental> findAll() {
        return csvFileReader.read(filePath).stream()
                .map(this::mapRowToRental)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Rental> findById(String id) {
        return findAll().stream()
                .filter(rental -> rental.getId().equalsIgnoreCase(id))
                .findFirst();
    }

    @Override
    public List<Rental> findByCustomerId(String customerId) {
        return findAll().stream()
                .filter(rental -> rental.getCustomer().getId().equalsIgnoreCase(customerId))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Rental> findActiveRentalByVehiclePlate(String plate) {
        return findAll().stream()
                .filter(rental -> rental.getVehicle().getPlate().equalsIgnoreCase(plate))
                .filter(rental -> rental.getStatus() == RentalStatus.ACTIVE)
                .findFirst();
    }

    @Override
    public List<Rental> findByStatus(RentalStatus status) {
        return findAll().stream()
                .filter(rental -> rental.getStatus() == status)
                .collect(Collectors.toList());
    }

    private void initializeFile() {
        csvFileWriter.write(filePath, HEADER, csvFileReader.read(filePath));
    }

    private void writeRentals(List<Rental> rentals) {
        List<List<String>> rows = rentals.stream()
                .map(rental -> Arrays.asList(
                        rental.getId(),
                        rental.getCustomer().getId(),
                        rental.getVehicle().getPlate(),
                        rental.getStartDate().toString(),
                        rental.getEndDate().toString(),
                        String.valueOf(rental.getTotalPrice()),
                        rental.getStatus().name(),
                        rental.getActualReturnDate() == null ? "" : rental.getActualReturnDate().toString(),
                        String.valueOf(rental.getLateFee()),
                        String.valueOf(rental.getFinalPrice())
                ))
                .collect(Collectors.toList());
        csvFileWriter.write(filePath, HEADER, rows);
    }

    private Rental mapRowToRental(List<String> row) {
        Customer customer = customerRepository.findById(row.get(1))
                .orElseThrow(() -> new IllegalStateException("CSV kiralama icin musteri bulunamadi: " + row.get(1)));
        Vehicle vehicle = vehicleRepository.findByPlate(row.get(2))
                .orElseThrow(() -> new IllegalStateException("CSV kiralama icin arac bulunamadi: " + row.get(2)));

        LocalDate actualReturnDate = row.get(7).isBlank() ? null : LocalDate.parse(row.get(7));

        return new Rental(
                row.get(0),
                customer,
                vehicle,
                LocalDate.parse(row.get(3)),
                LocalDate.parse(row.get(4)),
                Double.parseDouble(row.get(5)),
                RentalStatus.valueOf(row.get(6)),
                actualReturnDate,
                Double.parseDouble(row.get(8)),
                Double.parseDouble(row.get(9))
        );
    }
}
