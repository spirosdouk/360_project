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
 * @author spiros
 */
public class EditVehicleTable {

    public void deleteVehicle(String lic_plate) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String delete = "DELETE FROM vehicles WHERE lic_plate = '" + lic_plate + "'";
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

    public void updateVehicleField(String lic_plate, String field, String value) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String update = "UPDATE vehicles SET " + field + "='" + value + "' WHERE lic_plate = '" + lic_plate + "'";
        stmt.executeUpdate(update);
    }

    public String getVehicleFieldForAdmin() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs;
        String fields = "V.lic_plate, V.type, V.brand, V.model, V.isRented, V.is_damaged,";
        String query = "SELECT " + fields + " CASE WHEN M.status = 'ongoing' THEN 'ongoing' ELSE 'fine' END AS maintenance_status FROM vehicles V LEFT JOIN maintenance M ON V.lic_plate = M.lic_plate AND M.status = 'ongoing'";
        rs = stmt.executeQuery(query);
        StringBuilder resultBuilder = new StringBuilder();
        while (rs.next()) {
            // Assuming fields contains comma-separated column names.
            // This will concatenate the values of these columns for each row.
            String[] fieldNames = (fields + " maintenance_status").split(",");
            for (String field : fieldNames) {
                resultBuilder.append(rs.getString(field.trim())).append(",");
            }
            resultBuilder.append("|"); // New line for each row
        }
        rs.close();
        stmt.close();
        con.close();

        return resultBuilder.toString();
    }

    public String getVehicleRentalDuration() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs;
        String fields = "RentalYear, V.type, MinDuration, MaxDuration, AvgDuration";
        String query = "SELECT YEAR(R.rental_date) AS RentalYear, V.type, COALESCE(MIN(R.duration), 0) AS MinDuration, COALESCE(MAX(R.duration), 0) AS MaxDuration,  COALESCE(AVG(R.duration), 0) AS AvgDuration FROM vehicles V LEFT JOIN rentals R ON V.lic_plate = R.lic_plate WHERE R.rental_date IS NOT NULL GROUP BY V.type;";
        rs = stmt.executeQuery(query);
        StringBuilder resultBuilder = new StringBuilder();
        while (rs.next()) {
            // Assuming fields contains comma-separated column names.
            // This will concatenate the values of these columns for each row.
            String[] fieldNames = (fields).split(",");
            for (String field : fieldNames) {
                resultBuilder.append(rs.getString(field.trim())).append(",");
            }
            resultBuilder.append("|"); // New line for each row
        }
        rs.close();
        stmt.close();
        con.close();

        return resultBuilder.toString();
    }

    public String getPopularVehicle() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs;
        String fields = "V.lic_plate, V.type, V.brand, V.model, V.rented_count";
        String query = "SELECT V.lic_plate, V.type, V.brand, V.model, V.rented_count FROM vehicles V INNER JOIN (SELECT type, MAX(rented_count) AS MaxRentedCount FROM vehicles GROUP BY type) AS SubQuery ON V.type = SubQuery.type AND V.rented_count = SubQuery.MaxRentedCount;";
        rs = stmt.executeQuery(query);
        StringBuilder resultBuilder = new StringBuilder();
        while (rs.next()) {
            // Assuming fields contains comma-separated column names.
            // This will concatenate the values of these columns for each row.
            String[] fieldNames = (fields).split(",");
            for (String field : fieldNames) {
                resultBuilder.append(rs.getString(field.trim())).append(",");
            }
            resultBuilder.append("|"); // New line for each row
        }
        rs.close();
        stmt.close();
        con.close();

        return resultBuilder.toString();
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

    public void updateVehicleRentalStatus_date(String lic_plate, boolean isRented, int additionalDays) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        PreparedStatement pstmt = null;
        try {
            // First, get the current total_days from the database
            String selectQuery = "SELECT total_days FROM vehicles WHERE lic_plate = ?";
            pstmt = con.prepareStatement(selectQuery);
            pstmt.setString(1, lic_plate);
            ResultSet rs = pstmt.executeQuery();
            int currentTotalDays = 0;
            if(rs.next()) {
                currentTotalDays = rs.getInt("total_days");
            }
            rs.close();
            pstmt.close();

            int newTotalDays = currentTotalDays + additionalDays;

            String updateQuery = isRented
                    ? "UPDATE vehicles SET isRented = ?, total_days = ?, rented_count = rented_count + 1 WHERE lic_plate = ?"
                    : "UPDATE vehicles SET isRented = ?, total_days = ? WHERE lic_plate = ?";

            pstmt = con.prepareStatement(updateQuery);
            pstmt.setString(1, isRented ? "true" : "false");
            pstmt.setInt(2, newTotalDays); // Set the new total_days
            pstmt.setString(3, lic_plate);
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
            String carChange) throws SQLException, ClassNotFoundException {
        String vehicleType = getTypeOfVehicle(originalLicencePlate);

        ArrayList<Vehicle> availableVehicles = getAvailableVehiclesByType(vehicleType);
        // Assuming assign the first available vehicle
        if(!availableVehicles.isEmpty()) {
            Vehicle newVehicle = availableVehicles.get(0);
            String newLicensePlate = newVehicle.getLic_plate(); // Assuming getLicPlate() method exists

            Rental newRental = new Rental(username, drivLic, newLicensePlate, duration, dailyCost,
                    rentalDate, isReturned, hasInsurance, carChange);

            // Convert Rental object to JSON and add to database
            EditRentalTable rentalTable = new EditRentalTable();
            String rentalJson = rentalTable.RentalToJSON(newRental); // Assuming RentalToJSON method exists
            rentalTable.addRentalFromJSON(rentalJson);

            updateVehicleRentalStatus(newVehicle.getLic_plate(), true);

            return newVehicle; // return the assigned vehicle
        } else {
            // No available vehicles of the same type
            return null;
        }
    }

}
