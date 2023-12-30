/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mainClasses;

/**
 *
 * @author dimos
 */
public class Vehicle {
    int vehicle_id, range_km, rented_count, total_days, daily_rental_cost, daily_insurance_cost;
    String brand, model, color, type, is_damaged, lic_plate, sub_type, isRented;
    // Setter methods
    public void setVehicle_id(int vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    public void setRange_km(int range_km) {
        this.range_km = range_km;
    }

    public void setRented_count(int rented_count) {
        this.rented_count = rented_count;
    }

    public void setTotal_days(int total_days) {
        this.total_days = total_days;
    }

    public void setDaily_rental_cost(int daily_rental_cost) {
        this.daily_rental_cost = daily_rental_cost;
    }

    public void setDaily_insurance_cost(int daily_insurance_cost) {
        this.daily_insurance_cost = daily_insurance_cost;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setIs_damaged(String is_damaged) {
        this.is_damaged = is_damaged;
    }

    public void setLic_plate(String lic_plate) {
        this.lic_plate = lic_plate;
    }

    public void setSub_type(String sub_type) {
        this.sub_type = sub_type;
    }

    // Getter methods
    public String getSub_type() {
        return sub_type;
    }

    public String getLic_plate() {
        return lic_plate;
    }

    public int getVehicle_id() {
        return vehicle_id;
    }

    public int getRange_km() {
        return range_km;
    }

    public int getRented_count() {
        return rented_count;
    }

    public int getTotal_days() {
        return total_days;
    }

    public int getDaily_rental_cost() {
        return daily_rental_cost;
    }

    public int getDaily_insurance_cost() {
        return daily_insurance_cost;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getColor() {
        return color;
    }

    public String getType() {
        return type;
    }

    public String getIs_damaged() {
        return is_damaged;
    }
    public void setIsRented(String isRented) {
        this.isRented = isRented;
    }

    public String getIsRented() {
        return isRented;
    }

    public Vehicle(String _brand, String _model, String _color, String _type, String _lic_plate, int _range_km, int _daily_rental_cost, int _daily_insurance_cost, String _damaged,
            String _sub_type, String isRented) {
        this.brand = _brand;
        this.model = _model;
        this.color = _color;
        this.type = _type;
        this.lic_plate = _lic_plate;
        this.rented_count = 0;
        this.total_days = 0;
        this.range_km = _range_km;
        this.daily_rental_cost = _daily_rental_cost;
        this.daily_insurance_cost = _daily_insurance_cost;
        this.is_damaged = _damaged;
        this.sub_type = _sub_type;
        this.isRented = isRented;
    }

}
