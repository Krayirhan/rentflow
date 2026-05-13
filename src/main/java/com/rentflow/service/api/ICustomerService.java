package com.rentflow.service.api;

import com.rentflow.model.Customer;

import java.util.List;

public interface ICustomerService {

    void addCustomer(Customer customer);

    List<Customer> getAllCustomers();

    Customer getCustomerById(String id);
}
