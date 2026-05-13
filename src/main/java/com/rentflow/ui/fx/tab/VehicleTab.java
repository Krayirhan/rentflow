package com.rentflow.ui.fx.tab;

import com.rentflow.exception.RentFlowException;
import com.rentflow.model.Vehicle;
import com.rentflow.service.RentalService;
import com.rentflow.service.VehicleService;
import com.rentflow.ui.fx.dialog.AddVehicleDialog;
import com.rentflow.ui.fx.dialog.CalculatePriceDialog;
import com.rentflow.ui.fx.util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class VehicleTab {

    private VehicleService vehicleService;
    private RentalService rentalService;
    private TableView<Vehicle> vehicleTable;

    public VehicleTab(VehicleService vehicleService, RentalService rentalService) {
        this.vehicleService = vehicleService;
        this.rentalService = rentalService;
    }

    public Tab createTab() {
        Tab tab = new Tab();
        tab.setText("Araçlar");
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
        vehicleTable = createVehicleTable();
        ScrollPane scrollPane = new ScrollPane(vehicleTable);
        scrollPane.setFitToWidth(true);
        vbox.getChildren().add(scrollPane);

        refreshTable();
        return vbox;
    }

    private HBox createButtonBox() {
        HBox hbox = new HBox();
        hbox.setSpacing(10);

        Button addBtn = new Button("+ Araç Ekle");
        addBtn.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        addBtn.setOnAction(e -> showAddVehicleDialog());

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

        Label label = new Label("Plakaya Göre Ara:");
        TextField searchField = new TextField();
        searchField.setPromptText("Örn: 34ABC123");

        Button searchBtn = new Button("Ara");
        searchBtn.setOnAction(e -> {
            String plate = searchField.getText().trim();
            if (plate.isEmpty()) {
                refreshTable();
            } else {
                searchByPlate(plate);
            }
        });

        Button priceBtn = new Button("Kira Ücreti Hesapla");
        priceBtn.setOnAction(e -> showPriceDialog());

        hbox.getChildren().addAll(label, searchField, searchBtn, priceBtn);
        HBox.setHgrow(searchField, javafx.scene.layout.Priority.ALWAYS);
        return hbox;
    }

    private TableView<Vehicle> createVehicleTable() {
        TableView<Vehicle> table = new TableView<>();

        TableColumn<Vehicle, String> plateCol = new TableColumn<>("Plaka");
        plateCol.setCellValueFactory(p -> new javafx.beans.property.SimpleStringProperty(p.getValue().getPlate()));

        TableColumn<Vehicle, String> brandCol = new TableColumn<>("Marka");
        brandCol.setCellValueFactory(p -> new javafx.beans.property.SimpleStringProperty(p.getValue().getBrand()));

        TableColumn<Vehicle, String> modelCol = new TableColumn<>("Model");
        modelCol.setCellValueFactory(p -> new javafx.beans.property.SimpleStringProperty(p.getValue().getModel()));

        TableColumn<Vehicle, Integer> yearCol = new TableColumn<>("Yıl");
        yearCol.setCellValueFactory(p -> new javafx.beans.property.SimpleObjectProperty<>(p.getValue().getYear()));

        TableColumn<Vehicle, Double> priceCol = new TableColumn<>("Günlük Fiyat");
        priceCol.setCellValueFactory(p -> new javafx.beans.property.SimpleObjectProperty<>(p.getValue().getDailyPrice()));

        TableColumn<Vehicle, String> statusCol = new TableColumn<>("Durum");
        statusCol.setCellValueFactory(p -> new javafx.beans.property.SimpleStringProperty(
                p.getValue().isAvailable() ? "Müsait" : "Kiralanmış"
        ));

        table.getColumns().addAll(plateCol, brandCol, modelCol, yearCol, priceCol, statusCol);
        table.setPrefHeight(400);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        return table;
    }

    private void refreshTable() {
        try {
            List<Vehicle> vehicles = vehicleService.getAllVehicles();
            ObservableList<Vehicle> items = FXCollections.observableArrayList(vehicles);
            vehicleTable.setItems(items);
        } catch (Exception e) {
            AlertUtil.showError("Hata", "Araçlar yüklenirken hata oluştu: " + e.getMessage());
        }
    }

    private void searchByPlate(String plate) {
        try {
            Vehicle vehicle = vehicleService.getVehicleByPlate(plate);
            ObservableList<Vehicle> items = FXCollections.observableArrayList(vehicle);
            vehicleTable.setItems(items);
        } catch (RentFlowException e) {
            AlertUtil.showWarning("Bilgi", e.getMessage());
            refreshTable();
        }
    }

    private void showAddVehicleDialog() {
        AddVehicleDialog dialog = new AddVehicleDialog();
        if (dialog.showAndWait().orElse(false)) {
            try {
                dialog.addVehicle(vehicleService);
                AlertUtil.showSuccess("Başarılı", "Araç başarıyla eklendi.");
                refreshTable();
            } catch (RentFlowException e) {
                AlertUtil.showError("Hata", e.getMessage());
            }
        }
    }

    private void showPriceDialog() {
        CalculatePriceDialog dialog = new CalculatePriceDialog();
        if (dialog.showAndWait().orElse(false)) {
            try {
                double price = dialog.calculatePrice(vehicleService);
                AlertUtil.showSuccess("Kira Ücreti", "Toplam fiyat: ₺" + price);
            } catch (RentFlowException e) {
                AlertUtil.showError("Hata", e.getMessage());
            }
        }
    }
}
