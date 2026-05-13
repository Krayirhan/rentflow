package com.rentflow.ui.menu;

import com.rentflow.exception.ConsoleInputClosedException;
import com.rentflow.ui.controller.CustomerConsoleController;
import com.rentflow.ui.controller.RentalConsoleController;
import com.rentflow.ui.controller.VehicleConsoleController;
import com.rentflow.ui.input.InputReader;

public class ConsoleMenu {

    private InputReader inputReader;
    private VehicleConsoleController vehicleConsoleController;
    private CustomerConsoleController customerConsoleController;
    private RentalConsoleController rentalConsoleController;

    public ConsoleMenu(
            InputReader inputReader,
            VehicleConsoleController vehicleConsoleController,
            CustomerConsoleController customerConsoleController,
            RentalConsoleController rentalConsoleController
    ) {
        this.inputReader = inputReader;
        this.vehicleConsoleController = vehicleConsoleController;
        this.customerConsoleController = customerConsoleController;
        this.rentalConsoleController = rentalConsoleController;
    }

    public void start() {
        boolean running = true;

        while (running) {
            try {
                showMenu();

                String choice = inputReader.readString("Seciminiz: ");

                switch (choice) {
                    case "1":
                        vehicleConsoleController.listVehicles();
                        break;

                    case "2":
                        vehicleConsoleController.addVehicle();
                        break;

                    case "3":
                        vehicleConsoleController.findVehicleByPlate();
                        break;

                    case "4":
                        vehicleConsoleController.calculateRentalPrice();
                        break;

                    case "5":
                        rentalConsoleController.rentVehicle();
                        break;

                    case "6":
                        rentalConsoleController.returnVehicle();
                        break;

                    case "7":
                        customerConsoleController.listCustomers();
                        break;

                    case "8":
                        customerConsoleController.addCustomer();
                        break;

                    case "9":
                        customerConsoleController.findCustomerById();
                        break;

                    case "10":
                        rentalConsoleController.listRentals();
                        break;

                    case "11":
                        rentalConsoleController.listRentalsByCustomerId();
                        break;

                    case "12":
                        rentalConsoleController.listActiveRentals();
                        break;

                    case "13":
                        rentalConsoleController.listCompletedRentals();
                        break;

                    case "14":
                        vehicleConsoleController.listAvailableVehicles();
                        break;

                    case "15":
                        rentalConsoleController.showTotalRevenue();
                        break;

                    case "0":
                        System.out.println("Programdan cikiliyor...");
                        running = false;
                        break;

                    default:
                        System.out.println("Gecersiz secim.");
                }

            } catch (ConsoleInputClosedException exception) {
                System.out.println(exception.getMessage());
                running = false;
            }
        }
    }

    private void showMenu() {
        System.out.println();
        System.out.println("==== RentFlow ====");
        System.out.println("1 - Araclari listele");
        System.out.println("2 - Arac ekle");
        System.out.println("3 - Plakaya gore arac ara");
        System.out.println("4 - Kira ucreti hesapla");
        System.out.println("5 - Musteriye arac kirala");
        System.out.println("6 - Arac iade et");
        System.out.println("7 - Musterileri listele");
        System.out.println("8 - Musteri ekle");
        System.out.println("9 - Id'ye gore musteri ara");
        System.out.println("10 - Kiralamalari listele");
        System.out.println("11 - Musteriye gore kiralamalari listele");
        System.out.println("12 - Aktif kiralamalari listele");
        System.out.println("13 - Tamamlanan kiralamalari listele");
        System.out.println("14 - Musait araclari listele");
        System.out.println("15 - Toplam geliri goster");
        System.out.println("0 - Cikis");
    }
}
