package com.rentflow.ui.printer;

import com.rentflow.model.Customer;

import java.util.List;

public class CustomerPrinter {

    public void printCustomerList(List<Customer> customers) {
        System.out.println();
        System.out.println("Tum musteriler:");

        if (customers.isEmpty()) {
            System.out.println("Kayitli musteri yok.");
            return;
        }

        for (Customer customer : customers) {
            System.out.println(
                    customer.getId() + " - " +
                    customer.getFullName() + " - " +
                    customer.getPhoneNumber()
            );
        }
    }

    public void printCustomerDetails(Customer customer) {
        System.out.println();
        System.out.println("Musteri bulundu:");
        System.out.println("Id: " + customer.getId());
        System.out.println("Ad soyad: " + customer.getFullName());
        System.out.println("Telefon: " + customer.getPhoneNumber());
    }
}
