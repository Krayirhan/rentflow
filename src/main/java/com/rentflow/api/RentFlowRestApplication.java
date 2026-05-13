package com.rentflow.api;

import com.rentflow.repository.memory.InMemoryCustomerRepository;
import com.rentflow.repository.memory.InMemoryRentalRepository;
import com.rentflow.repository.memory.InMemoryVehicleRepository;
import com.rentflow.service.CustomerService;
import com.rentflow.service.RentalService;
import com.rentflow.service.VehicleService;
import com.rentflow.app.DataInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "com.rentflow")
public class RentFlowRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(RentFlowRestApplication.class, args);
    }

    @Bean
    public VehicleService vehicleService() {
        return new VehicleService(new InMemoryVehicleRepository());
    }

    @Bean
    public CustomerService customerService() {
        return new CustomerService(new InMemoryCustomerRepository());
    }

    @Bean
    public RentalService rentalService(
            VehicleService vehicleService,
            CustomerService customerService
    ) {
        return new RentalService(
                new InMemoryRentalRepository(),
                customerService,
                vehicleService
        );
    }

    @Bean
    public DataInitializer dataInitializer(
            VehicleService vehicleService,
            CustomerService customerService
    ) {
        DataInitializer.initialize(vehicleService, customerService);
        return new DataInitializer(vehicleService, customerService);
    }
}
