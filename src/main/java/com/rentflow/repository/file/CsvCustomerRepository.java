package com.rentflow.repository.file;

import com.rentflow.model.Customer;
import com.rentflow.persistence.CsvFileReader;
import com.rentflow.persistence.CsvFileWriter;
import com.rentflow.repository.CustomerRepository;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CsvCustomerRepository implements CustomerRepository {

    private static final List<String> HEADER = Arrays.asList("id", "fullName", "phoneNumber");

    private final Path filePath;
    private final CsvFileReader csvFileReader;
    private final CsvFileWriter csvFileWriter;

    public CsvCustomerRepository(Path filePath, CsvFileReader csvFileReader, CsvFileWriter csvFileWriter) {
        this.filePath = filePath;
        this.csvFileReader = csvFileReader;
        this.csvFileWriter = csvFileWriter;
        initializeFile();
    }

    @Override
    public void save(Customer customer) {
        List<Customer> customers = findAll();
        customers.removeIf(existingCustomer -> existingCustomer.getId().equalsIgnoreCase(customer.getId()));
        customers.add(customer);
        writeCustomers(customers);
    }

    @Override
    public List<Customer> findAll() {
        return csvFileReader.read(filePath).stream()
                .map(row -> new Customer(row.get(0), row.get(1), row.get(2)))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Customer> findById(String id) {
        return findAll().stream()
                .filter(customer -> customer.getId().equalsIgnoreCase(id))
                .findFirst();
    }

    private void initializeFile() {
        csvFileWriter.write(filePath, HEADER, csvFileReader.read(filePath));
    }

    private void writeCustomers(List<Customer> customers) {
        List<List<String>> rows = customers.stream()
                .map(customer -> Arrays.asList(
                        customer.getId(),
                        customer.getFullName(),
                        customer.getPhoneNumber()
                ))
                .collect(Collectors.toList());
        csvFileWriter.write(filePath, HEADER, rows);
    }
}
