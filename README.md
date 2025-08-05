# Car_Rental_System_App_Using_Java
This is a desktop Car Rental System application built using JavaFX. It provides an intuitive graphical user interface for managing car rentals.


# Car Rental System (JavaFX)

## Overview

This is a desktop **Car Rental System** application built with **JavaFX** providing an interactive graphical user interface to manage car rentals efficiently. The system allows users to view available cars, add new cars, rent cars by entering customer details, and return rented cars. It also features receipt popups for rental transactions.

---

## Features

- **View Available Cars:** Displays a list of cars available for rent with details like brand, model, and price per day.
- **Add New Cars:** Allows adding new cars to the inventory dynamically through a form.
- **Rent a Car:** Rent an available car by entering customer name, phone number, and rental duration.
- **View Rented Cars:** Shows a list of rented cars with customer information and rental duration.
- **Return Cars:** Return rented cars to update their availability status.
- **Rental Receipts:** Pop-up dialogs with rental details shown both on renting a car and via a button in the rented cars tab.

---

## Project Structure

- `com.carrental.model` — Contains data model classes (`Car`, `Customer`, `Rental`).
- `com.carrental.service` — Contains business logic and system operations (`CarRentalSystem`).
- `com.carrental.ui` — Contains JavaFX UI classes (`MainApp`).

---

## Prerequisites

- **Java 11** or later
- **JavaFX SDK** (download from [https://gluonhq.com/products/javafx/](https://gluonhq.com/products/javafx/))
- An IDE such as **IntelliJ IDEA** or **Eclipse** set up with JavaFX libraries.

---

## Setup and Running

1. **Clone the repository:**

git clone https://github.com/dharm0509/Car_Rental_System_App_Using_Java.git cd Car_Rental_System_App_Using_Java


2. **Download and extract** the JavaFX SDK from [GluonHQ JavaFX Downloads](https://gluonhq.com/products/javafx/).

3. **Configure your IDE:**

- Add the JavaFX `lib` directory as a library to the project.
- Set VM options for running the app:
  ```
  --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml
  ```
  Replace `/path/to/javafx-sdk/lib` with your actual JavaFX SDK lib path.

4. **Build and run** `MainApp.java`.

---

## Usage

- Navigate through the tabs:
- **Available Cars:** View and rent available cars.
- **Rented Cars:** View rented cars, return cars, or view rental receipts.
- **Add Car:** Add new cars to the system.

- Fill out required forms with valid data when renting or adding cars.

---

## Screenshots

<img width="1624" height="1180" alt="image" src="https://github.com/user-attachments/assets/98006479-545d-4e1d-8d44-1144561688e9" />

<img width="1536" height="1092" alt="image" src="https://github.com/user-attachments/assets/5eb7cae1-3793-4dfa-9852-90fa945807af" />

<img width="1624" height="1180" alt="image" src="https://github.com/user-attachments/assets/44e3e5c4-b5cb-41db-97d3-4b3bddd7fb33" />

<img width="944" height="756" alt="image" src="https://github.com/user-attachments/assets/90a1a07c-11fe-4665-bb63-2f2fe60afd71" />


---

## Author 

Dharm Rathod - PPSU - 22SE02ML063

---

## Acknowledgments

- JavaFX official documentation: [https://openjfx.io/](https://openjfx.io/)
- GluonHQ JavaFX SDK: [https://gluonhq.com/products/javafx/](https://gluonhq.com/products/javafx/)


