package com.rentflow.service;

import com.rentflow.exception.DuplicateResourceException;
import com.rentflow.exception.ResourceNotFoundException;
import com.rentflow.exception.ValidationException;
import com.rentflow.model.Customer;
import com.rentflow.repository.CustomerRepository;

import java.util.List;

public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void addCustomer(Customer customer) {
        validateCustomer(customer);

        boolean customerAlreadyExists = customerRepository.findById(customer.getId()).isPresent();

        if (customerAlreadyExists) {
            throw new DuplicateResourceException("Bu id'ye sahip bir musteri zaten var: " + customer.getId());
        }

        customerRepository.save(customer);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(String id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Musteri bulunamadi: " + id));
    }

    private void validateCustomer(Customer customer) {
        if (customer.getId() == null || customer.getId().isBlank()) {
            throw new ValidationException("Musteri id bos olamaz.");
        }

        if (customer.getFullName() == null || customer.getFullName().isBlank()) {
            throw new ValidationException("Musteri adi bos olamaz.");
        }

        if (customer.getPhoneNumber() == null || customer.getPhoneNumber().isBlank()) {
            throw new ValidationException("Telefon numarasi bos olamaz.");
        }
    }
}
