package com.rentflow.ui.printer;

import com.rentflow.model.Rental;

import java.util.List;

public class RentalPrinter {

    public void printRentalList(List<Rental> rentals) {
        printRentalList("Tum kiralamalar:", rentals);
    }

    public void printRentalList(String title, List<Rental> rentals) {
        System.out.println();
        System.out.println(title);

        if (rentals.isEmpty()) {
            System.out.println("Kayitli kiralama yok.");
            return;
        }

        for (Rental rental : rentals) {
            printRentalSummary(rental);
        }
    }

    public void printRentalDetails(Rental rental) {
        System.out.println();
        System.out.println("Kiralama detayi:");
        System.out.println("Kiralama id: " + rental.getId());
        System.out.println("Musteri: " + rental.getCustomer().getId() + " - " + rental.getCustomer().getFullName());
        System.out.println("Arac: " + rental.getVehicle().getPlate() + " - " +
                rental.getVehicle().getBrand() + " " + rental.getVehicle().getModel());
        System.out.println("Baslangic tarihi: " + rental.getStartDate());
        System.out.println("Bitis tarihi: " + rental.getEndDate());
        System.out.println("Toplam ucret: " + rental.getTotalPrice());
        System.out.println("Gercek iade tarihi: " + (rental.getActualReturnDate() == null ? "-" : rental.getActualReturnDate()));
        System.out.println("Gec iade ucreti: " + rental.getLateFee());
        System.out.println("Final ucret: " + rental.getFinalPrice());
        System.out.println("Durum: " + rental.getStatus());
    }

    private void printRentalSummary(Rental rental) {
        System.out.println(
                rental.getId() + " - " +
                rental.getCustomer().getFullName() + " - " +
                rental.getVehicle().getPlate() + " - " +
                rental.getStartDate() + " / " +
                rental.getEndDate() + " - " +
                rental.getFinalPrice() + " - " +
                rental.getStatus()
        );
    }
}
