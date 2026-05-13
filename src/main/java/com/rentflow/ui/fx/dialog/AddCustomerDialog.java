package com.rentflow.ui.fx.dialog;

import com.rentflow.model.Customer;
import com.rentflow.service.CustomerService;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class AddCustomerDialog extends Dialog<Boolean> {

    private TextField idField;
    private TextField nameField;
    private TextField phoneField;

    public AddCustomerDialog() {
        setTitle("Müşteri Ekle");
        setHeaderText(null);

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(15));
        vbox.setSpacing(10);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        idField = new TextField();
        idField.setPromptText("C-1");

        nameField = new TextField();
        nameField.setPromptText("Ahmet Yılmaz");

        phoneField = new TextField();
        phoneField.setPromptText("05551234567");

        grid.add(new Label("Müşteri ID:"), 0, 0);
        grid.add(idField, 1, 0);

        grid.add(new Label("Ad Soyad:"), 0, 1);
        grid.add(nameField, 1, 1);

        grid.add(new Label("Telefon:"), 0, 2);
        grid.add(phoneField, 1, 2);

        vbox.getChildren().add(grid);
        getDialogPane().setContent(vbox);

        ButtonType okButton = new ButtonType("Ekle", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("İptal", ButtonBar.ButtonData.CANCEL_CLOSE);
        getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

        setResultConverter(buttonType -> buttonType == okButton);
    }

    public void addCustomer(CustomerService customerService) {
        String id = idField.getText();
        String name = nameField.getText();
        String phone = phoneField.getText();

        Customer customer = new Customer(id, name, phone);
        customerService.addCustomer(customer);
    }
}
