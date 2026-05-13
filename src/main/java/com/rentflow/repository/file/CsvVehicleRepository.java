package com.rentflow.repository.file;

import com.rentflow.enums.VehicleStatus;
import com.rentflow.model.Car;
import com.rentflow.model.Motorcycle;
import com.rentflow.model.Vehicle;
import com.rentflow.persistence.CsvFileReader;
import com.rentflow.persistence.CsvFileWriter;
import com.rentflow.repository.VehicleRepository;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CsvVehicleRepository implements VehicleRepository {

    private static final List<String> HEADER = Arrays.asList(
            "plate", "type", "brand", "model", "year", "dailyPrice", "seatCount", "helmetIncluded", "status"
    );

    private final Path filePath;
    private final CsvFileReader csvFileReader;
    private final CsvFileWriter csvFileWriter;

    public CsvVehicleRepository(Path filePath, CsvFileReader csvFileReader, CsvFileWriter csvFileWriter) {
        this.filePath = filePath;
        this.csvFileReader = csvFileReader;
        this.csvFileWriter = csvFileWriter;
        initializeFile();
    }

    @Override
    public void save(Vehicle vehicle) {
        List<Vehicle> vehicles = findAll();
        vehicles.removeIf(existingVehicle -> existingVehicle.getPlate().equalsIgnoreCase(vehicle.getPlate()));
        vehicles.add(vehicle);
        writeVehicles(vehicles);
    }

    @Override
    public List<Vehicle> findAll() {
        return readRows().stream()
                .map(this::mapRowToVehicle)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public Optional<Vehicle> findByPlate(String plate) {
        return findAll().stream()
                .filter(vehicle -> vehicle.getPlate().equalsIgnoreCase(plate))
                .findFirst();
    }

    @Override
    public List<Vehicle> findByStatus(VehicleStatus status) {
        return findAll().stream()
                .filter(vehicle -> vehicle.getStatus() == status)
                .collect(Collectors.toList());
    }

    private void initializeFile() {
        csvFileWriter.write(filePath, HEADER, readRows());
    }

    private List<List<String>> readRows() {
        return csvFileReader.read(filePath);
    }

    private void writeVehicles(List<Vehicle> vehicles) {
        List<List<String>> rows = vehicles.stream()
                .map(this::mapVehicleToRow)
                .collect(Collectors.toList());
        csvFileWriter.write(filePath, HEADER, rows);
    }

    private List<String> mapVehicleToRow(Vehicle vehicle) {
        if (vehicle instanceof Car car) {
            return Arrays.asList(
                    car.getPlate(),
                    "CAR",
                    car.getBrand(),
                    car.getModel(),
                    String.valueOf(car.getYear()),
                    String.valueOf(car.getDailyPrice()),
                    String.valueOf(car.getSeatCount()),
                    "",
                    car.getStatus().name()
            );
        }

        Motorcycle motorcycle = (Motorcycle) vehicle;
        return Arrays.asList(
                motorcycle.getPlate(),
                "MOTORCYCLE",
                motorcycle.getBrand(),
                motorcycle.getModel(),
                String.valueOf(motorcycle.getYear()),
                String.valueOf(motorcycle.getDailyPrice()),
                "",
                String.valueOf(motorcycle.isHelmetIncluded()),
                motorcycle.getStatus().name()
        );
    }

    private Vehicle mapRowToVehicle(List<String> row) {
        String plate = row.get(0);
        String type = row.get(1);
        String brand = row.get(2);
        String model = row.get(3);
        int year = Integer.parseInt(row.get(4));
        double dailyPrice = Double.parseDouble(row.get(5));
        VehicleStatus status = VehicleStatus.valueOf(row.get(8));

        if ("CAR".equalsIgnoreCase(type)) {
            int seatCount = Integer.parseInt(row.get(6));
            return new Car(plate, brand, model, year, dailyPrice, seatCount, status);
        }

        boolean helmetIncluded = Boolean.parseBoolean(row.get(7));
        return new Motorcycle(plate, brand, model, year, dailyPrice, helmetIncluded, status);
    }
}
