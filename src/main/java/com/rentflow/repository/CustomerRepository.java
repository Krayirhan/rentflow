package com.rentflow.repository;

import com.rentflow.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository {

    void save(Customer customer);

    List<Customer> findAll();

    Optional<Customer> findById(String id);
}
