package com.rentflow.ui.controller;

import com.rentflow.model.Car;
import com.rentflow.model.Motorcycle;
import com.rentflow.model.Vehicle;
import com.rentflow.service.VehicleService;
import com.rentflow.ui.input.InputReader;
import com.rentflow.ui.printer.VehiclePrinter;

import java.util.List;

public class VehicleConsoleController {

    private final VehicleService vehicleService;
    private final InputReader inputReader;
    private final VehiclePrinter vehiclePrinter;

    public VehicleConsoleController(VehicleService vehicleService, InputReader inputReader, VehiclePrinter vehiclePrinter) {
        this.vehicleService = vehicleService;
        this.inputReader = inputReader;
        this.vehiclePrinter = vehiclePrinter;
    }

    public void listVehicles() {
        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        vehiclePrinter.printVehicleList(vehicles);
    }

    public void listAvailableVehicles() {
        List<Vehicle> vehicles = vehicleService.getAvailableVehicles();
        vehiclePrinter.printVehicleList("Musait araclar:", vehicles);
    }

    public void addVehicle() {
        System.out.println();
        System.out.println("Arac tipi secin");
        System.out.println("1 - Car");
        System.out.println("2 - Motorcycle");

        String vehicleType = inputReader.readString("Seciminiz: ");

        try {
            switch (vehicleType) {
                case "1":
                    addCar();
                    break;
                case "2":
                    addMotorcycle();
                    break;
                default:
                    System.out.println("Gecersiz arac tipi.");
            }
        } catch (NumberFormatException exception) {
            System.out.println("Hatali giris. Yil, gunluk fiyat ve sayisal alanlar sayi olmalidir.");
        } catch (IllegalArgumentException exception) {
            System.out.println("Hata: " + exception.getMessage());
        }
    }

    public void findVehicleByPlate() {
        System.out.println();
        System.out.println("Plakaya gore arac arama");

        try {
            String plate = inputReader.readString("Plaka: ");
            Vehicle vehicle = vehicleService.getVehicleByPlate(plate);
            vehiclePrinter.printVehicleDetails(vehicle);
        } catch (IllegalArgumentException exception) {
            System.out.println("Hata: " + exception.getMessage());
        }
    }

    public void calculateRentalPrice() {
        System.out.println();
        System.out.println("Kira ucreti hesaplama");

        try {
            String plate = inputReader.readString("Plaka: ");
            int days = inputReader.readInt("Gun sayisi: ");
            double totalPrice = vehicleService.calculateRentalPrice(plate, days);
            System.out.println("Toplam kira ucreti: " + totalPrice);
        } catch (NumberFormatException exception) {
            System.out.println("Hatali giris. Gun sayisi sayisal olmalidir.");
        } catch (IllegalArgumentException exception) {
            System.out.println("Hata: " + exception.getMessage());
        }
    }

    public void rentVehicle() {
        System.out.println();
        System.out.println("Arac kiralama");

        try {
            String plate = inputReader.readString("Plaka: ");
            vehicleService.rentVehicle(plate);
            System.out.println("Arac basariyla kiralandi.");
        } catch (IllegalArgumentException exception) {
            System.out.println("Hata: " + exception.getMessage());
        }
    }

    public void returnVehicle() {
        System.out.println();
        System.out.println("Arac iade etme");

        try {
            String plate = inputReader.readString("Plaka: ");
            vehicleService.returnVehicle(plate);
            System.out.println("Arac basariyla iade edildi.");
        } catch (IllegalArgumentException exception) {
            System.out.println("Hata: " + exception.getMessage());
        }
    }

    private void addCar() {
        System.out.println();
        System.out.println("Yeni araba ekleme");

        String plate = inputReader.readString("Plaka: ");
        String brand = inputReader.readString("Marka: ");
        String model = inputReader.readString("Model: ");
        int year = inputReader.readInt("Yil: ");
        double dailyPrice = inputReader.readDouble("Gunluk fiyat: ");
        int seatCount = inputReader.readInt("Koltuk sayisi: ");

        Car car = new Car(plate, brand, model, year, dailyPrice, seatCount);
        vehicleService.addVehicle(car);

        System.out.println("Araba basariyla eklendi.");
    }

    private void addMotorcycle() {
        System.out.println();
        System.out.println("Yeni motosiklet ekleme");

        String plate = inputReader.readString("Plaka: ");
        String brand = inputReader.readString("Marka: ");
        String model = inputReader.readString("Model: ");
        int year = inputReader.readInt("Yil: ");
        double dailyPrice = inputReader.readDouble("Gunluk fiyat: ");
        boolean helmetIncluded = inputReader.readYesNo("Kask dahil mi? e/h: ");

        Motorcycle motorcycle = new Motorcycle(plate, brand, model, year, dailyPrice, helmetIncluded);
        vehicleService.addVehicle(motorcycle);

        System.out.println("Motosiklet basariyla eklendi.");
    }
}
