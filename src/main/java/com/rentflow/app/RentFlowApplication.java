package com.rentflow.app;

import com.rentflow.repository.CustomerRepository;
import com.rentflow.repository.RentalRepository;
import com.rentflow.repository.VehicleRepository;
import com.rentflow.repository.memory.InMemoryCustomerRepository;
import com.rentflow.repository.memory.InMemoryRentalRepository;
import com.rentflow.repository.memory.InMemoryVehicleRepository;
import com.rentflow.service.CustomerService;
import com.rentflow.service.RentalService;
import com.rentflow.service.VehicleService;
import com.rentflow.ui.fx.MainWindow;
import javafx.application.Application;
import javafx.stage.Stage;

public class RentFlowApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Dependency Injection - Console'daki gibi
        VehicleRepository vehicleRepository = new InMemoryVehicleRepository();
        CustomerRepository customerRepository = new InMemoryCustomerRepository();
        RentalRepository rentalRepository = new InMemoryRentalRepository();

        VehicleService vehicleService = new VehicleService(vehicleRepository);
        CustomerService customerService = new CustomerService(customerRepository);
        RentalService rentalService = new RentalService(
                rentalRepository,
                customerService,
                vehicleService
        );

        // Initialize sample data
        DataInitializer.initialize(vehicleService, customerService);

        // Show JavaFX main window
        MainWindow mainWindow = new MainWindow(
                vehicleService,
                customerService,
                rentalService
        );

        mainWindow.show(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
