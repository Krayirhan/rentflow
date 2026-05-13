package com.rentflow.app;

import com.rentflow.model.Car;
import com.rentflow.model.Customer;
import com.rentflow.service.CustomerService;
import com.rentflow.service.VehicleService;

public class DataInitializer {

    private final VehicleService vehicleService;
    private final CustomerService customerService;

    public DataInitializer(
            VehicleService vehicleService,
            CustomerService customerService
    ) {
        this.vehicleService = vehicleService;
        this.customerService = customerService;
    }

    public static void initialize(VehicleService vehicleService, CustomerService customerService) {
        DataInitializer initializer = new DataInitializer(vehicleService, customerService);
        initializer.seed();
    }

    public void seed() {
        if (!vehicleService.getAllVehicles().isEmpty() || !customerService.getAllCustomers().isEmpty()) {
            return;
        }

        seedVehicles();
        seedCustomers();
    }

    private void seedVehicles() {
        Car car1 = new Car(
                "34ABC123",
                "Toyota",
                "Corolla",
                2020,
                1500,
                5
        );

        Car car2 = new Car(
                "06DEF456",
                "Honda",
                "Civic",
                2021,
                1800,
                5
        );

        vehicleService.addVehicle(car1);
        vehicleService.addVehicle(car2);
    }

    private void seedCustomers() {
        Customer customer1 = new Customer(
                "C-1",
                "Ahmet Yilmaz",
                "05551234567"
        );

        Customer customer2 = new Customer(
                "C-2",
                "Ayse Demir",
                "05559876543"
        );

        customerService.addCustomer(customer1);
        customerService.addCustomer(customer2);
    }
}
