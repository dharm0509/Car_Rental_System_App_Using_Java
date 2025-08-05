package com.carrental.service;
import com.carrental.model.*;
import java.util.*;

public class CarRentalSystem {
    private List<Car> cars = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private List<Rental> rentals = new ArrayList<>();

    public void addCar(Car car) { cars.add(car); }
    public void addCustomer(Customer customer) { customers.add(customer); }
    public void rentCar(Car car, Customer customer, int days) {
        if(car.isAvailable()) {
            car.rent();
            rentals.add(new Rental(car, customer, days));
        }
    }
    public void returnCar(Car car) {
        car.returnCar();
        rentals.removeIf(rental -> rental.getCar() == car);
    }

    public List<Car> getCars() { return cars; }
    public List<Rental> getRentals() { return rentals; }
}
