package com.rentflow.repository.memory;

import com.rentflow.model.Customer;
import com.rentflow.repository.CustomerRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryCustomerRepository implements CustomerRepository {

    private final List<Customer> customers = new ArrayList<>();

    @Override
    public void save(Customer customer) {
        findById(customer.getId()).ifPresent(customers::remove);
        customers.add(customer);
    }

    @Override
    public List<Customer> findAll() {
        return new ArrayList<>(customers);
    }

    @Override
    public Optional<Customer> findById(String id) {
        return customers.stream()
                .filter(customer -> customer.getId().equalsIgnoreCase(id))
                .findFirst();
    }
}
