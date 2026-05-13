package com.rentflow.ui.fx.dialog;

import com.rentflow.service.VehicleService;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class CalculatePriceDialog extends Dialog<Boolean> {

    private TextField plateField;
    private Spinner<Integer> daysSpinner;

    public CalculatePriceDialog() {
        setTitle("Kira Ücreti Hesapla");
        setHeaderText(null);

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(15));
        vbox.setSpacing(10);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        plateField = new TextField();
        plateField.setPromptText("34ABC123");

        daysSpinner = new Spinner<>(1, 365, 3);

        grid.add(new Label("Araç Plakası:"), 0, 0);
        grid.add(plateField, 1, 0);

        grid.add(new Label("Kira Günü Sayısı:"), 0, 1);
        grid.add(daysSpinner, 1, 1);

        vbox.getChildren().add(grid);
        getDialogPane().setContent(vbox);

        ButtonType okButton = new ButtonType("Hesapla", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("İptal", ButtonBar.ButtonData.CANCEL_CLOSE);
        getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

        setResultConverter(buttonType -> buttonType == okButton);
    }

    public double calculatePrice(VehicleService vehicleService) {
        String plate = plateField.getText();
        int days = daysSpinner.getValue();

        return vehicleService.calculateRentalPrice(plate, days);
    }
}
