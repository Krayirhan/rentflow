package com.rentflow.ui.controller;

import com.rentflow.model.Rental;
import com.rentflow.service.RentalService;
import com.rentflow.ui.input.InputReader;
import com.rentflow.ui.printer.RentalPrinter;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class RentalConsoleController {

    private RentalService rentalService;
    private InputReader inputReader;
    private RentalPrinter rentalPrinter;

    public RentalConsoleController(
            RentalService rentalService,
            InputReader inputReader,
            RentalPrinter rentalPrinter
    ) {
        this.rentalService = rentalService;
        this.inputReader = inputReader;
        this.rentalPrinter = rentalPrinter;
    }

    public void rentVehicle() {
        System.out.println();
        System.out.println("Musteriye arac kiralama");

        try {
            String customerId = inputReader.readString("Musteri id: ");
            String plate = inputReader.readString("Plaka: ");

            System.out.println("Tarih formati: yyyy-MM-dd");
            LocalDate startDate = readDate("Baslangic tarihi: ");
            LocalDate endDate = readDate("Bitis tarihi: ");

            Rental rental = rentalService.rentVehicle(
                    customerId,
                    plate,
                    startDate,
                    endDate
            );

            System.out.println("Arac basariyla kiralandi.");
            rentalPrinter.printRentalDetails(rental);

        } catch (DateTimeParseException exception) {
            System.out.println("Hatali tarih formati. Ornek format: 2026-05-10");
        } catch (IllegalArgumentException exception) {
            System.out.println("Hata: " + exception.getMessage());
        }
    }

    public void returnVehicle() {
        System.out.println();
        System.out.println("Arac iade etme");

        try {
            String plate = inputReader.readString("Plaka: ");
            String actualReturnDateText = inputReader.readString("Gercek iade tarihi (yyyy-MM-dd, bos ise bugun): ");

            LocalDate actualReturnDate = actualReturnDateText.isBlank()
                    ? LocalDate.now()
                    : LocalDate.parse(actualReturnDateText);

            Rental rental = rentalService.returnVehicle(plate, actualReturnDate);

            System.out.println("Arac basariyla iade edildi.");
            rentalPrinter.printRentalDetails(rental);

        } catch (DateTimeParseException exception) {
            System.out.println("Hatali tarih formati. Ornek format: 2026-05-10");
        } catch (IllegalArgumentException exception) {
            System.out.println("Hata: " + exception.getMessage());
        }
    }

    public void listRentals() {
        List<Rental> rentals = rentalService.getAllRentals();
        rentalPrinter.printRentalList(rentals);
    }

    public void listActiveRentals() {
        rentalPrinter.printRentalList("Aktif kiralamalar:", rentalService.getActiveRentals());
    }

    public void listCompletedRentals() {
        rentalPrinter.printRentalList("Tamamlanan kiralamalar:", rentalService.getCompletedRentals());
    }

    public void listRentalsByCustomerId() {
        System.out.println();
        System.out.println("Musteriye gore kiralama listeleme");

        try {
            String customerId = inputReader.readString("Musteri id: ");

            List<Rental> rentals = rentalService.getRentalsByCustomerId(customerId);

            rentalPrinter.printRentalList(rentals);

        } catch (IllegalArgumentException exception) {
            System.out.println("Hata: " + exception.getMessage());
        }
    }

    public void showTotalRevenue() {
        System.out.println();
        System.out.println("Toplam gelir: " + rentalService.getTotalRevenue());
    }

    private LocalDate readDate(String message) {
        String value = inputReader.readString(message);
        return LocalDate.parse(value);
    }
}
