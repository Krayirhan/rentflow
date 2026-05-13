package com.rentflow.ui.printer;

import com.rentflow.model.Car;
import com.rentflow.model.Motorcycle;
import com.rentflow.model.Vehicle;

import java.util.List;

public class VehiclePrinter {

    public void printVehicleList(List<Vehicle> vehicles) {
        printVehicleList("Tum araclar:", vehicles);
    }

    public void printVehicleList(String title, List<Vehicle> vehicles) {
        System.out.println();
        System.out.println(title);

        if (vehicles.isEmpty()) {
            System.out.println("Kayitli arac yok.");
            return;
        }

        for (Vehicle vehicle : vehicles) {
            System.out.println(
                    vehicle.getClass().getSimpleName() + " - " +
                    vehicle.getPlate() + " - " +
                    vehicle.getBrand() + " " +
                    vehicle.getModel() + " - " +
                    vehicle.getStatus()
            );
        }
    }

    public void printVehicleDetails(Vehicle vehicle) {
        System.out.println("Arac bulundu:");
        System.out.println("Tip: " + vehicle.getClass().getSimpleName());
        System.out.println("Plaka: " + vehicle.getPlate());
        System.out.println("Marka: " + vehicle.getBrand());
        System.out.println("Model: " + vehicle.getModel());
        System.out.println("Yil: " + vehicle.getYear());
        System.out.println("Gunluk fiyat: " + vehicle.getDailyPrice());
        System.out.println("Durum: " + vehicle.getStatus());

        if (vehicle instanceof Car car) {
            System.out.println("Koltuk sayisi: " + car.getSeatCount());
        }

        if (vehicle instanceof Motorcycle motorcycle) {
            System.out.println("Kask dahil mi: " + motorcycle.isHelmetIncluded());
        }
    }
}
