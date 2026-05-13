package com.rentflow.ui.fx.tab;

import com.rentflow.enums.RentalStatus;
import com.rentflow.exception.RentFlowException;
import com.rentflow.model.Rental;
import com.rentflow.service.CustomerService;
import com.rentflow.service.RentalService;
import com.rentflow.service.VehicleService;
import com.rentflow.ui.fx.dialog.RentVehicleDialog;
import com.rentflow.ui.fx.util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.stream.Collectors;

public class RentalTab {

    private RentalService rentalService;
    private VehicleService vehicleService;
    private CustomerService customerService;
    private TableView<Rental> rentalTable;
    private ComboBox<String> statusFilter;

    public RentalTab(RentalService rentalService, VehicleService vehicleService, CustomerService customerService) {
        this.rentalService = rentalService;
        this.vehicleService = vehicleService;
        this.customerService = customerService;
    }

    public Tab createTab() {
        Tab tab = new Tab();
        tab.setText("Kiralamalar");
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

        // Middle - Filter
        HBox filterBox = createFilterBox();
        vbox.getChildren().add(filterBox);

        // Bottom - Table
        rentalTable = createRentalTable();
        ScrollPane scrollPane = new ScrollPane(rentalTable);
        scrollPane.setFitToWidth(true);
        vbox.getChildren().add(scrollPane);

        refreshTable();
        return vbox;
    }

    private HBox createButtonBox() {
        HBox hbox = new HBox();
        hbox.setSpacing(10);

        Button rentBtn = new Button("+ Kiralama Yap");
        rentBtn.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        rentBtn.setOnAction(e -> showRentDialog());

        Button returnBtn = new Button("🔄 İade Et");
        returnBtn.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        returnBtn.setOnAction(e -> returnSelectedVehicle());

        Button refreshBtn = new Button("Yenile");
        refreshBtn.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        refreshBtn.setOnAction(e -> refreshTable());

        hbox.getChildren().addAll(rentBtn, returnBtn, refreshBtn);
        return hbox;
    }

    private HBox createFilterBox() {
        HBox hbox = new HBox();
        hbox.setSpacing(10);
        hbox.setPadding(new Insets(5));
        hbox.setStyle("-fx-background-color: #ecf0f1;");

        Label label = new Label("Durum:");
        statusFilter = new ComboBox<>();
        statusFilter.setItems(FXCollections.observableArrayList("Tümü", "Aktif", "Tamamlandı"));
        statusFilter.setValue("Tümü");
        statusFilter.setOnAction(e -> applyFilter());

        hbox.getChildren().addAll(label, statusFilter);
        return hbox;
    }

    private TableView<Rental> createRentalTable() {
        TableView<Rental> table = new TableView<>();

        TableColumn<Rental, String> idCol = new TableColumn<>("Kiralama ID");
        idCol.setCellValueFactory(p -> new javafx.beans.property.SimpleStringProperty(p.getValue().getId()));

        TableColumn<Rental, String> customerCol = new TableColumn<>("Müşteri");
        customerCol.setCellValueFactory(p -> new javafx.beans.property.SimpleStringProperty(p.getValue().getCustomer().getFullName()));

        TableColumn<Rental, String> vehicleCol = new TableColumn<>("Araç");
        vehicleCol.setCellValueFactory(p -> new javafx.beans.property.SimpleStringProperty(
                p.getValue().getVehicle().getPlate()
        ));

        TableColumn<Rental, String> startDateCol = new TableColumn<>("Başlangıç");
        startDateCol.setCellValueFactory(p -> new javafx.beans.property.SimpleStringProperty(p.getValue().getStartDate().toString()));

        TableColumn<Rental, String> endDateCol = new TableColumn<>("Bitiş");
        endDateCol.setCellValueFactory(p -> new javafx.beans.property.SimpleStringProperty(p.getValue().getEndDate().toString()));

        TableColumn<Rental, Double> priceCol = new TableColumn<>("Toplam Ücret");
        priceCol.setCellValueFactory(p -> new javafx.beans.property.SimpleObjectProperty<>(p.getValue().getTotalPrice()));

        TableColumn<Rental, String> statusCol = new TableColumn<>("Durum");
        statusCol.setCellValueFactory(p -> new javafx.beans.property.SimpleStringProperty(
                p.getValue().getStatus() == RentalStatus.ACTIVE ? "Aktif" : "Tamamlandı"
        ));

        table.getColumns().addAll(idCol, customerCol, vehicleCol, startDateCol, endDateCol, priceCol, statusCol);
        table.setPrefHeight(400);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        return table;
    }

    private void refreshTable() {
        try {
            List<Rental> rentals = rentalService.getAllRentals();
            ObservableList<Rental> items = FXCollections.observableArrayList(rentals);
            rentalTable.setItems(items);
        } catch (Exception e) {
            AlertUtil.showError("Hata", "Kiralamalar yüklenirken hata oluştu: " + e.getMessage());
        }
    }

    private void applyFilter() {
        try {
            List<Rental> rentals = rentalService.getAllRentals();
            String filter = statusFilter.getValue();

            if (!"Tümü".equals(filter)) {
                rentals = rentals.stream()
                        .filter(r -> {
                            if ("Aktif".equals(filter)) {
                                return r.getStatus() == RentalStatus.ACTIVE;
                            } else {
                                return r.getStatus() == RentalStatus.COMPLETED;
                            }
                        })
                        .collect(Collectors.toList());
            }

            ObservableList<Rental> items = FXCollections.observableArrayList(rentals);
            rentalTable.setItems(items);
        } catch (Exception e) {
            AlertUtil.showError("Hata", "Filtre uygulanırken hata oluştu: " + e.getMessage());
        }
    }

    private void showRentDialog() {
        RentVehicleDialog dialog = new RentVehicleDialog();
        if (dialog.showAndWait().orElse(false)) {
            try {
                dialog.rentVehicle(rentalService, customerService, vehicleService);
                AlertUtil.showSuccess("Başarılı", "Kiralama başarıyla oluşturuldu.");
                refreshTable();
            } catch (RentFlowException e) {
                AlertUtil.showError("Hata", e.getMessage());
            }
        }
    }

    private void returnSelectedVehicle() {
        Rental selected = rentalTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtil.showWarning("Uyarı", "Lütfen bir kiralama seçin.");
            return;
        }

        if (selected.getStatus() != RentalStatus.ACTIVE) {
            AlertUtil.showWarning("Uyarı", "Sadece aktif kiralamalar iade edilebilir.");
            return;
        }

        try {
            rentalService.returnVehicle(selected.getVehicle().getPlate());
            AlertUtil.showSuccess("Başarılı", "Araç başarıyla iade edildi.");
            refreshTable();
        } catch (RentFlowException e) {
            AlertUtil.showError("Hata", e.getMessage());
        }
    }
}
