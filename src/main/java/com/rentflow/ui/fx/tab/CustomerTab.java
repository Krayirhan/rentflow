package com.rentflow.ui.fx.tab;

import com.rentflow.exception.RentFlowException;
import com.rentflow.model.Customer;
import com.rentflow.service.CustomerService;
import com.rentflow.ui.fx.dialog.AddCustomerDialog;
import com.rentflow.ui.fx.util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class CustomerTab {

    private CustomerService customerService;
    private TableView<Customer> customerTable;

    public CustomerTab(CustomerService customerService) {
        this.customerService = customerService;
    }

    public Tab createTab() {
        Tab tab = new Tab();
        tab.setText("Müşteriler");
        tab.setClosable(false);
        tab.setContent(createContent());
        return tab;
    }

    private VBox createContent() {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(15));
        vbox.setSpacing(10);

        // Top - Buttons
        HBox buttonBox = createButtonBox();
        vbox.getChildren().add(buttonBox);

        // Middle - Search
        HBox searchBox = createSearchBox();
        vbox.getChildren().add(searchBox);

        // Bottom - Table
        customerTable = createCustomerTable();
        ScrollPane scrollPane = new ScrollPane(customerTable);
        scrollPane.setFitToWidth(true);
        vbox.getChildren().add(scrollPane);

        refreshTable();
        return vbox;
    }

    private HBox createButtonBox() {
        HBox hbox = new HBox();
        hbox.setSpacing(10);

        Button addBtn = new Button("+ Müşteri Ekle");
        addBtn.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        addBtn.setOnAction(e -> showAddCustomerDialog());

        Button refreshBtn = new Button("Yenile");
        refreshBtn.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        refreshBtn.setOnAction(e -> refreshTable());

        hbox.getChildren().addAll(addBtn, refreshBtn);
        return hbox;
    }

    private HBox createSearchBox() {
        HBox hbox = new HBox();
        hbox.setSpacing(10);
        hbox.setPadding(new Insets(5));
        hbox.setStyle("-fx-background-color: #ecf0f1;");

        Label label = new Label("ID'ye Göre Ara:");
        TextField searchField = new TextField();
        searchField.setPromptText("Örn: C-1");

        Button searchBtn = new Button("Ara");
        searchBtn.setOnAction(e -> {
            String id = searchField.getText().trim();
            if (id.isEmpty()) {
                refreshTable();
            } else {
                searchById(id);
            }
        });

        hbox.getChildren().addAll(label, searchField, searchBtn);
        HBox.setHgrow(searchField, javafx.scene.layout.Priority.ALWAYS);
        return hbox;
    }

    private TableView<Customer> createCustomerTable() {
        TableView<Customer> table = new TableView<>();

        TableColumn<Customer, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(p -> new javafx.beans.property.SimpleStringProperty(p.getValue().getId()));

        TableColumn<Customer, String> nameCol = new TableColumn<>("Ad Soyad");
        nameCol.setCellValueFactory(p -> new javafx.beans.property.SimpleStringProperty(p.getValue().getFullName()));

        TableColumn<Customer, String> phoneCol = new TableColumn<>("Telefon");
        phoneCol.setCellValueFactory(p -> new javafx.beans.property.SimpleStringProperty(p.getValue().getPhoneNumber()));

        table.getColumns().addAll(idCol, nameCol, phoneCol);
        table.setPrefHeight(400);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        return table;
    }

    private void refreshTable() {
        try {
            List<Customer> customers = customerService.getAllCustomers();
            ObservableList<Customer> items = FXCollections.observableArrayList(customers);
            customerTable.setItems(items);
        } catch (Exception e) {
            AlertUtil.showError("Hata", "Müşteriler yüklenirken hata oluştu: " + e.getMessage());
        }
    }

    private void searchById(String id) {
        try {
            Customer customer = customerService.getCustomerById(id);
            ObservableList<Customer> items = FXCollections.observableArrayList(customer);
            customerTable.setItems(items);
        } catch (RentFlowException e) {
            AlertUtil.showWarning("Bilgi", e.getMessage());
            refreshTable();
        }
    }

    private void showAddCustomerDialog() {
        AddCustomerDialog dialog = new AddCustomerDialog();
        if (dialog.showAndWait().orElse(false)) {
            try {
                dialog.addCustomer(customerService);
                AlertUtil.showSuccess("Başarılı", "Müşteri başarıyla eklendi.");
                refreshTable();
            } catch (RentFlowException e) {
                AlertUtil.showError("Hata", e.getMessage());
            }
        }
    }
}
