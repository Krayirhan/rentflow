package com.rentflow.repository.jpa.impl;

import com.rentflow.model.Customer;
import com.rentflow.repository.CustomerRepository;
import com.rentflow.repository.jpa.JpaCustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@ConditionalOnProperty(name = "rentflow.persistence", havingValue = "database", matchIfMissing = false)
public class JpaCustomerRepositoryImpl implements CustomerRepository {

    @Autowired
    private JpaCustomerRepository jpaCustomerRepository;

    @Override
    public void save(Customer customer) {
        jpaCustomerRepository.save(customer);
    }

    @Override
    public List<Customer> findAll() {
        return jpaCustomerRepository.findAll();
    }

    @Override
    public Optional<Customer> findById(String id) {
        return jpaCustomerRepository.findById(id);
    }
}
