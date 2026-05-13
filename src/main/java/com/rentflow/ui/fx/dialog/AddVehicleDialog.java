package com.rentflow.ui.fx.dialog;

import com.rentflow.model.Car;
import com.rentflow.model.Motorcycle;
import com.rentflow.service.VehicleService;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class AddVehicleDialog extends Dialog<Boolean> {

    private TextField plateField;
    private TextField brandField;
    private TextField modelField;
    private Spinner<Integer> yearSpinner;
    private Spinner<Double> priceSpinner;
    private ComboBox<String> typeCombo;
    private Spinner<Integer> seatsSpinner;
    private CheckBox trunkCheckBox;

    public AddVehicleDialog() {
        setTitle("Araç Ekle");
        setHeaderText(null);

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(15));
        vbox.setSpacing(10);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        plateField = new TextField();
        plateField.setPromptText("34ABC123");

        brandField = new TextField();
        brandField.setPromptText("Toyota");

        modelField = new TextField();
        modelField.setPromptText("Corolla");

        yearSpinner = new Spinner<>(1950, 2026, 2020);
        priceSpinner = new Spinner<>(0.0, 10000.0, 1500.0, 100.0);
        priceSpinner.setEditable(true);

        typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll("Araba", "Motosiklet");
        typeCombo.setValue("Araba");

        seatsSpinner = new Spinner<>(2, 10, 5);
        trunkCheckBox = new CheckBox("Bagaj Var");
        trunkCheckBox.setSelected(true);

        grid.add(new Label("Plaka:"), 0, 0);
        grid.add(plateField, 1, 0);

        grid.add(new Label("Marka:"), 0, 1);
        grid.add(brandField, 1, 1);

        grid.add(new Label("Model:"), 0, 2);
        grid.add(modelField, 1, 2);

        grid.add(new Label("Yıl:"), 0, 3);
        grid.add(yearSpinner, 1, 3);

        grid.add(new Label("Günlük Fiyat (₺):"), 0, 4);
        grid.add(priceSpinner, 1, 4);

        grid.add(new Label("Araç Tipi:"), 0, 5);
        grid.add(typeCombo, 1, 5);

        grid.add(new Label("Koltuk Sayısı:"), 0, 6);
        grid.add(seatsSpinner, 1, 6);

        grid.add(trunkCheckBox, 1, 7);

        vbox.getChildren().add(grid);
        getDialogPane().setContent(vbox);

        ButtonType okButton = new ButtonType("Ekle", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("İptal", ButtonBar.ButtonData.CANCEL_CLOSE);
        getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

        setResultConverter(buttonType -> buttonType == okButton);
    }

    public void addVehicle(VehicleService vehicleService) {
        String plate = plateField.getText();
        String brand = brandField.getText();
        String model = modelField.getText();
        int year = yearSpinner.getValue();
        double price = priceSpinner.getValue();
        String type = typeCombo.getValue();

        if ("Araba".equals(type)) {
            int seats = seatsSpinner.getValue();
            Car car = new Car(plate, brand, model, year, price, seats);
            vehicleService.addVehicle(car);
        } else {
            boolean helmetIncluded = true;
            Motorcycle motorcycle = new Motorcycle(plate, brand, model, year, price, helmetIncluded);
            vehicleService.addVehicle(motorcycle);
        }
    }
}
