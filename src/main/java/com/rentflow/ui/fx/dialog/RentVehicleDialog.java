package com.rentflow.ui.fx.dialog;

import com.rentflow.service.CustomerService;
import com.rentflow.service.RentalService;
import com.rentflow.service.VehicleService;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.time.LocalDate;

public class RentVehicleDialog extends Dialog<Boolean> {

    private TextField customerIdField;
    private TextField plateField;
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;

    public RentVehicleDialog() {
        setTitle("Araç Kirala");
        setHeaderText(null);

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(15));
        vbox.setSpacing(10);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        customerIdField = new TextField();
        customerIdField.setPromptText("C-1");

        plateField = new TextField();
        plateField.setPromptText("34ABC123");

        startDatePicker = new DatePicker();
        startDatePicker.setValue(LocalDate.now());

        endDatePicker = new DatePicker();
        endDatePicker.setValue(LocalDate.now().plusDays(3));

        grid.add(new Label("Müşteri ID:"), 0, 0);
        grid.add(customerIdField, 1, 0);

        grid.add(new Label("Araç Plakası:"), 0, 1);
        grid.add(plateField, 1, 1);

        grid.add(new Label("Başlangıç Tarihi:"), 0, 2);
        grid.add(startDatePicker, 1, 2);

        grid.add(new Label("Bitiş Tarihi:"), 0, 3);
        grid.add(endDatePicker, 1, 3);

        vbox.getChildren().add(grid);
        getDialogPane().setContent(vbox);

        ButtonType okButton = new ButtonType("Kirala", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("İptal", ButtonBar.ButtonData.CANCEL_CLOSE);
        getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

        setResultConverter(buttonType -> buttonType == okButton);
    }

    public void rentVehicle(
            RentalService rentalService,
            CustomerService customerService,
            VehicleService vehicleService
    ) {
        String customerId = customerIdField.getText();
        String plate = plateField.getText();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        rentalService.rentVehicle(customerId, plate, startDate, endDate);
    }
}
