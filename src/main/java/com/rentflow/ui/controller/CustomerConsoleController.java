package com.rentflow.ui.controller;

import com.rentflow.model.Customer;
import com.rentflow.service.CustomerService;
import com.rentflow.ui.input.InputReader;
import com.rentflow.ui.printer.CustomerPrinter;

import java.util.List;

public class CustomerConsoleController {

    private CustomerService customerService;
    private InputReader inputReader;
    private CustomerPrinter customerPrinter;

    public CustomerConsoleController(
            CustomerService customerService,
            InputReader inputReader,
            CustomerPrinter customerPrinter
    ) {
        this.customerService = customerService;
        this.inputReader = inputReader;
        this.customerPrinter = customerPrinter;
    }

    public void listCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        customerPrinter.printCustomerList(customers);
    }

    public void addCustomer() {
        System.out.println();
        System.out.println("Yeni musteri ekleme");

        try {
            String id = inputReader.readString("Musteri id: ");
            String fullName = inputReader.readString("Ad soyad: ");
            String phoneNumber = inputReader.readString("Telefon: ");

            Customer customer = new Customer(id, fullName, phoneNumber);

            customerService.addCustomer(customer);

            System.out.println("Musteri basariyla eklendi.");

        } catch (IllegalArgumentException exception) {
            System.out.println("Hata: " + exception.getMessage());
        }
    }

    public void findCustomerById() {
        System.out.println();
        System.out.println("Id'ye gore musteri arama");

        try {
            String id = inputReader.readString("Musteri id: ");

            Customer customer = customerService.getCustomerById(id);

            customerPrinter.printCustomerDetails(customer);

        } catch (IllegalArgumentException exception) {
            System.out.println("Hata: " + exception.getMessage());
        }
    }
}
