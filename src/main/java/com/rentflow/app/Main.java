package com.rentflow.app;

import com.rentflow.persistence.CsvFileReader;
import com.rentflow.persistence.CsvFileWriter;
import com.rentflow.persistence.FilePathProvider;
import com.rentflow.repository.CustomerRepository;
import com.rentflow.repository.RentalRepository;
import com.rentflow.repository.VehicleRepository;
import com.rentflow.repository.file.CsvCustomerRepository;
import com.rentflow.repository.file.CsvRentalRepository;
import com.rentflow.repository.file.CsvVehicleRepository;
import com.rentflow.repository.memory.InMemoryCustomerRepository;
import com.rentflow.repository.memory.InMemoryRentalRepository;
import com.rentflow.repository.memory.InMemoryVehicleRepository;
import com.rentflow.service.CustomerService;
import com.rentflow.service.RentalService;
import com.rentflow.service.VehicleService;
import com.rentflow.ui.controller.CustomerConsoleController;
import com.rentflow.ui.controller.RentalConsoleController;
import com.rentflow.ui.controller.VehicleConsoleController;
import com.rentflow.ui.input.InputReader;
import com.rentflow.ui.menu.ConsoleMenu;
import com.rentflow.ui.printer.CustomerPrinter;
import com.rentflow.ui.printer.RentalPrinter;
import com.rentflow.ui.printer.VehiclePrinter;

import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        boolean useCsvRepository = args.length > 0 && "--csv".equalsIgnoreCase(args[0]);
        VehicleRepository vehicleRepository;
        CustomerRepository customerRepository;
        RentalRepository rentalRepository;

        if (useCsvRepository) {
            FilePathProvider filePathProvider = new FilePathProvider(Path.of("data"));
            CsvFileReader csvFileReader = new CsvFileReader();
            CsvFileWriter csvFileWriter = new CsvFileWriter();

            vehicleRepository = new CsvVehicleRepository(
                    filePathProvider.getVehiclesFilePath(),
                    csvFileReader,
                    csvFileWriter
            );
            customerRepository = new CsvCustomerRepository(
                    filePathProvider.getCustomersFilePath(),
                    csvFileReader,
                    csvFileWriter
            );
            rentalRepository = new CsvRentalRepository(
                    filePathProvider.getRentalsFilePath(),
                    csvFileReader,
                    csvFileWriter,
                    customerRepository,
                    vehicleRepository
            );
        } else {
            vehicleRepository = new InMemoryVehicleRepository();
            customerRepository = new InMemoryCustomerRepository();
            rentalRepository = new InMemoryRentalRepository();
        }

        VehicleService vehicleService = new VehicleService(vehicleRepository);
        CustomerService customerService = new CustomerService(customerRepository);
        RentalService rentalService = new RentalService(
                rentalRepository,
                customerService,
                vehicleService
        );

        InputReader inputReader = new InputReader();

        VehiclePrinter vehiclePrinter = new VehiclePrinter();
        CustomerPrinter customerPrinter = new CustomerPrinter();
        RentalPrinter rentalPrinter = new RentalPrinter();

        VehicleConsoleController vehicleConsoleController = new VehicleConsoleController(
                vehicleService,
                inputReader,
                vehiclePrinter
        );

        CustomerConsoleController customerConsoleController = new CustomerConsoleController(
                customerService,
                inputReader,
                customerPrinter
        );

        RentalConsoleController rentalConsoleController = new RentalConsoleController(
                rentalService,
                inputReader,
                rentalPrinter
        );

        DataInitializer dataInitializer = new DataInitializer(
                vehicleService,
                customerService
        );

        dataInitializer.seed();

        ConsoleMenu consoleMenu = new ConsoleMenu(
                inputReader,
                vehicleConsoleController,
                customerConsoleController,
                rentalConsoleController
        );

        consoleMenu.start();
    }
}
