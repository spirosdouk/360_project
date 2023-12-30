/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database_tables;

import com.google.gson.Gson;
import database_connect.DB_Connection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import mainClasses.Vehicle;
import mainClasses.Rental;
import java.sql.PreparedStatement;

/**
 *
 * @author dimos
 */
public class EditVehicleTable {

    public void deleteVehicle(String id) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String delete = "DELETE FROM vehicles WHERE vehicle_id = '" + id + "'";
        stmt.executeUpdate(delete);
    }

    public ArrayList<Vehicle> getAllVehicles() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ArrayList<Vehicle> tmp = new ArrayList<>();
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM vehicles");
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                Vehicle _vehicle = gson.fromJson(json, Vehicle.class);
                tmp.add(_vehicle);
            }
            return tmp;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
        }
        return null;
    }

    public void updateVehicleField(String username, String field, String value) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String update = "UPDATE vehicles SET " + field + "='" + value + "' WHERE username = '" + username + "'";
        stmt.executeUpdate(update);
    }

    public void updateVehicleRentalStatus(String lic_plate, boolean isRented) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        PreparedStatement pstmt = null;
        System.out.println(lic_plate);
        try {
            String updateQuery = "UPDATE vehicles SET isRented = ? WHERE lic_plate = ?";
            pstmt = con.prepareStatement(updateQuery);
            pstmt.setString(1, isRented ? "true" : "false");
            pstmt.setString(2, lic_plate);
            pstmt.executeUpdate();
            System.out.println("# Vehicle rental status updated in the database.");
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        } finally {
            if(pstmt != null) {
                pstmt.close();
            }
            if(con != null) {
                con.close();
            }
        }
    }
    public void updateDamageStatus(String lic_plate, boolean isDamaged) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        PreparedStatement pstmt = null;
        System.out.println(lic_plate);
        try {
            String updateQuery = "UPDATE vehicles SET is_damaged = ? WHERE lic_plate = ?";
            pstmt = con.prepareStatement(updateQuery);
            pstmt.setString(1, isDamaged ? "true" : "false");
            pstmt.setString(2, lic_plate);
            pstmt.executeUpdate();
            System.out.println("# Vehicle is_damaged status updated in the database.");
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        } finally {
            if(pstmt != null) {
                pstmt.close();
            }
            if(con != null) {
                con.close();
            }
        }
    }
    public void createVehicleTable() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        String query = "CREATE TABLE vehicles "
                + "    (brand VARCHAR(30) not null,"
                + "    model VARCHAR(30) not null,"
                + "    color VARCHAR(30) not null,"
                + "    type VARCHAR(15) not null,"
                + "    lic_plate VARCHAR(10) not null unique,"
                + "    range_km INTEGER not null,"
                + "    rented_count INT,"
                + "    total_days INT,"
                + "    daily_rental_cost INT not null,"
                + "    daily_insurance_cost INT not null,"
                + "    is_damaged VARCHAR(15) not null,"
                + "    isRented VARCHAR(15) not null," // Add the new column here
                + "    subtype_name VARCHAR(30),"
                + "    FOREIGN KEY (subtype_name) REFERENCES subtype(subtype_name),"
                + " PRIMARY KEY (lic_plate))";
        stmt.execute(query);
        stmt.close();
    }

    public void addNewVehicle(Vehicle _vehicle) throws ClassNotFoundException {
        try {
            Connection con = DB_Connection.getConnection();

            Statement stmt = con.createStatement();

            String subtypeValue = _vehicle.getSub_type();
            if (!_vehicle.getType().equals("car")) {
                subtypeValue = "NULL";
            }

            String insertQuery = "INSERT INTO "
                    + " vehicles (brand, model, color, type, lic_plate, range_km, rented_count, total_days, daily_rental_cost, daily_insurance_cost, is_damaged, isRented, subtype_name)"
                    + " VALUES ("
                    + "'" + _vehicle.getBrand() + "',"
                    + "'" + _vehicle.getModel() + "',"
                    + "'" + _vehicle.getColor() + "',"
                    + "'" + _vehicle.getType() + "',"
                    + "'" + _vehicle.getLic_plate() + "',"
                    + "'" + _vehicle.getRange_km() + "',"
                    + "'" + _vehicle.getRented_count() + "',"
                    + "'" + _vehicle.getTotal_days() + "',"
                    + "'" + _vehicle.getDaily_rental_cost() + "',"
                    + "'" + _vehicle.getDaily_insurance_cost() + "',"
                    + "'" + _vehicle.getIs_damaged() + "',"
                    + "'" + _vehicle.getIsRented() + "',"
                    + "'" + subtypeValue + "'"
                    + ")";
            //stmt.execute(table);
            System.out.println(insertQuery);
            stmt.executeUpdate(insertQuery);
            System.out.println("# The vehicle was successfully added in the database.");

            /* Get the member id from the database and set it to the member */
            stmt.close();

        } catch (SQLException ex) {
            Logger.getLogger(EditVehicleTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public String getTypeOfVehicle(String licence) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String vehicleType = null;

        try {
            String query = "SELECT type FROM vehicles WHERE lic_plate = ?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, licence);
            rs = pstmt.executeQuery();

            if(rs.next()) {
                vehicleType = rs.getString("type");
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        } finally {
            if(rs != null) {
                rs.close();
            }
            if(pstmt != null) {
                pstmt.close();
            }
            if(con != null) {
                con.close();
            }
        }

        return vehicleType;
    }

    public ArrayList<Vehicle> getAvailableVehiclesByType(String vehicleType) throws SQLException, ClassNotFoundException {
        ArrayList<Vehicle> vehicles = new ArrayList<>();
        Connection con = DB_Connection.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            String query = "SELECT * FROM vehicles WHERE type = ? AND isRented = 'false' AND is_damaged = 'false'";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, vehicleType);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                Vehicle vehicle = gson.fromJson(json, Vehicle.class);
                vehicles.add(vehicle);
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        } finally {
            if(rs != null) {
                rs.close();
            }
            if(pstmt != null) {
                pstmt.close();
            }
            if(con != null) {
                con.close();
            }
        }

        return vehicles;
    }

    public Vehicle assignNewVehicle(String originalLicencePlate, String username, int drivLic, int duration,
            int dailyCost, String rentalDate, String isReturned, String hasInsurance,
            int carChange) throws SQLException, ClassNotFoundException {
        // Get the type of the original vehicle
        String vehicleType = getTypeOfVehicle(originalLicencePlate);

        // Find available vehicles of the same type
        ArrayList<Vehicle> availableVehicles = getAvailableVehiclesByType(vehicleType);

        // Assuming you'll assign the first available vehicle (you can implement other selection logic)
    if(!availableVehicles.isEmpty()) {
        Vehicle newVehicle = availableVehicles.get(0);
        String newLicensePlate = newVehicle.getLic_plate(); // Assuming getLicPlate() method exists

        // Create a new Rental instance
        Rental newRental = new Rental(username, drivLic, newLicensePlate, duration, dailyCost,
                rentalDate, isReturned, hasInsurance, carChange);

        // Convert Rental object to JSON and add to database
        EditRentalTable rentalTable = new EditRentalTable();
        String rentalJson = rentalTable.RentalToJSON(newRental); // Assuming RentalToJSON method exists
        rentalTable.addRentalFromJSON(rentalJson);

        // Optionally, update the vehicle's status as rented
        updateVehicleRentalStatus(newVehicle.getLic_plate(), true);

        return newVehicle; // return the assigned vehicle
    } else {

            // No available vehicles of the same type
            return null;
        }
    }

}
