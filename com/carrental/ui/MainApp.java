package com.carrental.ui;

import com.carrental.model.*;
import com.carrental.service.CarRentalSystem;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.Optional;

public class MainApp extends Application {
    private final CarRentalSystem system = new CarRentalSystem();
    private TableView<Car> carTable = new TableView<>();
    private TableView<Rental> rentalTable = new TableView<>();

    @Override
    public void start(Stage primaryStage) {
        // Sample cars
        system.addCar(new Car("C001", "Toyota", "Camry", 60.0));
        system.addCar(new Car("C002", "Honda", "Accord", 70.0));
        system.addCar(new Car("C003", "Mahindra", "Thar", 150.0));

        TabPane tabPane = new TabPane();
        tabPane.getTabs().addAll(
                new Tab("Available Cars", createAvailableCarsPane()),
                new Tab("Rented Cars", createRentedCarsPane()),
                new Tab("Add Car", createAddCarPane())
        );
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        primaryStage.setTitle("Car Rental System");
        primaryStage.setScene(new Scene(tabPane, 700, 450));
        primaryStage.show();

        refreshCarTable();
        refreshRentalTable();
    }

    private BorderPane createAvailableCarsPane() {
        carTable = new TableView<>();

        TableColumn<Car, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("carID"));

        TableColumn<Car, String> brandCol = new TableColumn<>("Brand");
        brandCol.setCellValueFactory(new PropertyValueFactory<>("brand"));

        TableColumn<Car, String> modelCol = new TableColumn<>("Model");
        modelCol.setCellValueFactory(new PropertyValueFactory<>("model"));

        TableColumn<Car, Double> priceCol = new TableColumn<>("Price/Day");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("basePricePerDay"));

        carTable.getColumns().addAll(idCol, brandCol, modelCol, priceCol);

        Button rentBtn = new Button("Rent Selected Car");
        rentBtn.setOnAction(e -> showRentDialog());

        VBox buttons = new VBox(10, rentBtn);
        buttons.setAlignment(Pos.CENTER);

        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(10));
        pane.setCenter(carTable);
        pane.setBottom(buttons);
        BorderPane.setAlignment(buttons, Pos.CENTER);

        return pane;
    }

    private BorderPane createRentedCarsPane() {
        rentalTable = new TableView<>();

        TableColumn<Rental, String> idCol = new TableColumn<>("Car ID");
        idCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCar().getCarID()));

        TableColumn<Rental, String> brandCol = new TableColumn<>("Brand");
        brandCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCar().getBrand()));

        TableColumn<Rental, String> modelCol = new TableColumn<>("Model");
        modelCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCar().getModel()));

        TableColumn<Rental, String> custCol = new TableColumn<>("Customer");
        custCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCustomer().getCustomerName()));

        TableColumn<Rental, String> phoneCol = new TableColumn<>("Phone Number");
        phoneCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCustomer().getPhoneNumber()));

        TableColumn<Rental, Integer> daysCol = new TableColumn<>("Days");
        daysCol.setCellValueFactory(new PropertyValueFactory<>("days"));

        // Receipt Button column
        TableColumn<Rental, Void> receiptCol = new TableColumn<>("Receipt");
        receiptCol.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("Show Receipt");
            {
                btn.setOnAction(e -> {
                    Rental rental = getTableView().getItems().get(getIndex());
                    if (rental != null) {
                        showReceiptPopup(
                                rental.getCar(),
                                rental.getCustomer(),
                                rental.getDays(),
                                rental.getCar().calculatePrice(rental.getDays())
                        );
                    }
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                }
            }
        });

        rentalTable.getColumns().addAll(idCol, brandCol, modelCol, custCol, phoneCol, daysCol, receiptCol);

        Button returnBtn = new Button("Return Selected Car");
        returnBtn.setOnAction(e -> handleReturnCar());

        VBox buttons = new VBox(10, returnBtn);
        buttons.setAlignment(Pos.CENTER);

        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(10));
        pane.setCenter(rentalTable);
        pane.setBottom(buttons);
        BorderPane.setAlignment(buttons, Pos.CENTER);

        return pane;
    }

    private BorderPane createAddCarPane() {
        GridPane form = new GridPane();
        form.setPadding(new Insets(10));
        form.setHgap(10);
        form.setVgap(10);
        form.setAlignment(Pos.CENTER);

        TextField idField = new TextField();
        TextField brandField = new TextField();
        TextField modelField = new TextField();
        TextField priceField = new TextField();

        form.add(new Label("Car ID:"), 0, 0);
        form.add(idField, 1, 0);

        form.add(new Label("Brand:"), 0, 1);
        form.add(brandField, 1, 1);

        form.add(new Label("Model:"), 0, 2);
        form.add(modelField, 1, 2);

        form.add(new Label("Price per Day:"), 0, 3);
        form.add(priceField, 1, 3);

        Button addBtn = new Button("Add Car");
        addBtn.setOnAction(e -> {
            String id = idField.getText().trim();
            String brand = brandField.getText().trim();
            String model = modelField.getText().trim();
            String priceText = priceField.getText().trim();

            if (id.isEmpty() || brand.isEmpty() || model.isEmpty() || priceText.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Please fill all fields.");
                return;
            }

            double price;
            try {
                price = Double.parseDouble(priceText);
                if (price <= 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Enter a valid positive number for price.");
                return;
            }

            boolean exists = system.getCars().stream().anyMatch(c -> c.getCarID().equals(id));
            if (exists) {
                showAlert(Alert.AlertType.ERROR, "Car ID already exists.");
                return;
            }

            Car newCar = new Car(id, brand, model, price);
            system.addCar(newCar);
            showAlert(Alert.AlertType.INFORMATION, "Car added successfully!");
            idField.clear();
            brandField.clear();
            modelField.clear();
            priceField.clear();

            refreshCarTable();
        });

        VBox vbox = new VBox(15, form, addBtn);
        vbox.setAlignment(Pos.CENTER);
        BorderPane pane = new BorderPane(vbox);
        pane.setPadding(new Insets(20));

        return pane;
    }

    private void showRentDialog() {
        Car selectedCar = carTable.getSelectionModel().getSelectedItem();
        if (selectedCar == null || !selectedCar.isAvailable()) {
            showAlert(Alert.AlertType.ERROR, "No car selected or car not available.");
            return;
        }
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Rent Car");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField nameField = new TextField();
        TextField phoneField = new TextField();
        TextField daysField = new TextField();

        grid.add(new Label("Your Name:"), 0, 0);
        grid.add(nameField, 1, 0);

        grid.add(new Label("Phone Number:"), 0, 1);
        grid.add(phoneField, 1, 1);

        grid.add(new Label("Rental Days:"), 0, 2);
        grid.add(daysField, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(b -> b == ButtonType.OK);

        Optional<Boolean> result = dialog.showAndWait();

        if (result.isPresent() && result.get()) {
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            int days;

            if (name.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Please enter your name.");
                return;
            }
            if (phone.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Please enter your phone number.");
                return;
            }

            try {
                days = Integer.parseInt(daysField.getText().trim());
                if (days <= 0) throw new NumberFormatException();
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Please enter a valid positive number of rental days.");
                return;
            }

            Customer customer = new Customer("CUS" + (system.getRentals().size() + 1), name, phone);
            system.addCustomer(customer);

            double total = selectedCar.calculatePrice(days);

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirm Rental");
            confirm.setHeaderText(null);
            confirm.setContentText(
                    "Confirm Rental?\n" +
                            "Car: " + selectedCar.getBrand() + " " + selectedCar.getModel() + "\n" +
                            "Days: " + days + "\n" +
                            String.format("Total Price: $%.2f", total)
            );
            Optional<ButtonType> confirmed = confirm.showAndWait();
            if (confirmed.isPresent() && confirmed.get() == ButtonType.OK) {
                system.rentCar(selectedCar, customer, days);
                showAlert(Alert.AlertType.INFORMATION, "Car rented successfully!");
                refreshCarTable();
                refreshRentalTable();

                showReceiptPopup(selectedCar, customer, days, total);
            }
        }
    }

    private void showReceiptPopup(Car car, Customer customer, int days, double totalPrice) {
        Alert receiptAlert = new Alert(Alert.AlertType.INFORMATION);
        receiptAlert.setTitle("Rental Receipt");
        receiptAlert.setHeaderText("Rental Successful");

        String receipt = String.format(
                "Customer: %s\nPhone: %s\n\nCar: %s %s (ID: %s)\nRental Days: %d\nTotal Price: $%.2f",
                customer.getCustomerName(),
                customer.getPhoneNumber(),
                car.getBrand(),
                car.getModel(),
                car.getCarID(),
                days,
                totalPrice
        );

        receiptAlert.setContentText(receipt);
        receiptAlert.showAndWait();
    }

    private void handleReturnCar() {
        Rental selectedRental = rentalTable.getSelectionModel().getSelectedItem();
        if (selectedRental == null) {
            showAlert(Alert.AlertType.ERROR, "No rental selected.");
            return;
        }
        system.returnCar(selectedRental.getCar());
        showAlert(Alert.AlertType.INFORMATION, "Car returned successfully.");
        refreshCarTable();
        refreshRentalTable();
    }

    private void refreshCarTable() {
        carTable.getItems().clear();
        for (Car car : system.getCars()) {
            if (car.isAvailable()) carTable.getItems().add(car);
        }
    }

    private void refreshRentalTable() {
        rentalTable.getItems().clear();
        rentalTable.getItems().addAll(system.getRentals());
    }

    private void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }
}
