package com.rentflow.ui.fx;

import com.rentflow.service.CustomerService;
import com.rentflow.service.RentalService;
import com.rentflow.service.VehicleService;
import com.rentflow.ui.fx.tab.CustomerTab;
import com.rentflow.ui.fx.tab.RentalTab;
import com.rentflow.ui.fx.tab.VehicleTab;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainWindow {

    private VehicleService vehicleService;
    private CustomerService customerService;
    private RentalService rentalService;

    public MainWindow(
            VehicleService vehicleService,
            CustomerService customerService,
            RentalService rentalService
    ) {
        this.vehicleService = vehicleService;
        this.customerService = customerService;
        this.rentalService = rentalService;
    }

    public void show(Stage primaryStage) {
        // Main layout
        BorderPane root = new BorderPane();

        // Header
        HBox header = createHeader();
        root.setTop(header);

        // Tabs
        TabPane tabPane = createTabPane();
        root.setCenter(tabPane);

        // Scene
        Scene scene = new Scene(root, 1000, 700);
        primaryStage.setTitle("RentFlow - Araç Kiralama Sistemi");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private HBox createHeader() {
        HBox header = new HBox();
        header.setPadding(new Insets(15));
        header.setStyle("-fx-background-color: #2c3e50; -fx-font-size: 16px;");

        Text title = new Text("RentFlow - Araç Kiralama Yönetim Sistemi");
        title.setStyle("-fx-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");

        header.getChildren().add(title);
        return header;
    }

    private TabPane createTabPane() {
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // Vehicle Tab
        VehicleTab vehicleTab = new VehicleTab(vehicleService, rentalService);
        tabPane.getTabs().add(vehicleTab.createTab());

        // Customer Tab
        CustomerTab customerTab = new CustomerTab(customerService);
        tabPane.getTabs().add(customerTab.createTab());

        // Rental Tab
        RentalTab rentalTab = new RentalTab(rentalService, vehicleService, customerService);
        tabPane.getTabs().add(rentalTab.createTab());

        return tabPane;
    }
}
