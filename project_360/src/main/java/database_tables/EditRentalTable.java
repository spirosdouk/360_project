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
import mainClasses.Rental;
import java.sql.PreparedStatement;

/**
 *
 * @author spiros
 */
public class EditRentalTable {
    public void addRentalFromJSON(String json) throws ClassNotFoundException {
        System.out.println("sucesfully added");
        Rental rental = jsonToRental(json);
        addNewRental(rental);
    }

    public String RentalToJSON(Rental rental) {
        Gson gson = new Gson();

        String json = gson.toJson(rental, Rental.class);
        return json;
    }

    public Rental jsonToRental(String json) {
        Gson gson = new Gson();

        Rental rental = gson.fromJson(json, Rental.class);
        return rental;
    }
    public void deleteRental(String id) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String delete = "DELETE FROM rentals WHERE rental_id = '" + id + "'";
        stmt.executeUpdate(delete);
    }
    public ArrayList<Rental> getRentalsByUsername(String username) throws SQLException, ClassNotFoundException {
        ArrayList<Rental> rentals = new ArrayList<>();
        Connection con = DB_Connection.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            String query = "SELECT * FROM rentals WHERE username = ?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                Rental rental = gson.fromJson(json, Rental.class);
                rentals.add(rental);
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

        return rentals;
    }
    public ArrayList<Rental> getAllRentals() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ArrayList<Rental> tmp = new ArrayList<>();
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM rentals");
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                Rental _rent = gson.fromJson(json, Rental.class);
                tmp.add(_rent);
            }
            return tmp;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
        }
        return null;
    }

    public void updateRentalReturnStatus(String licPlate, boolean isReturned) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        PreparedStatement pstmt = null;
        try {
            String updateQuery = "UPDATE rentals SET is_returned = ? WHERE lic_plate = ?";
            pstmt = con.prepareStatement(updateQuery);
            pstmt.setString(1, isReturned ? "true" : "false");
            pstmt.setString(2, licPlate);
            pstmt.executeUpdate();
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
    public void updateRentalReturnStatus(String lic_plate, double newTotalCost, String isReturned) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        try {
            String updateQuery = "UPDATE rentals SET total_cost = ?, is_returned = ? WHERE lic_plate = ?";
            PreparedStatement pstmt = con.prepareStatement(updateQuery);
            pstmt.setDouble(1, newTotalCost);
            pstmt.setString(2, isReturned);
            pstmt.setString(3, lic_plate);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        } finally {
            if(con != null) {
                con.close();
            }
        }
    }

    public void createRentalTable() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String sql = "CREATE TABLE rentals "
                + "(rental_id INTEGER not NULL AUTO_INCREMENT, "
                + " lic_plate VARCHAR(10) not NULL,"
                + " username  VARCHAR(15) not NULL, "
                + " driv_lic INTEGER not NULL, "
                + " rental_date DATE not NULL, "
                + " duration INTEGER not NULL, "
                + " daily_cost INTEGER not NULL, "
                + " total_cost DOUBLE not NULL, "
                + " is_returned VARCHAR(15) not NULL, "
                + " has_insurance VARCHAR(15) not NULL, "
                + " car_change VARCHAR(15) , "
                + "FOREIGN KEY (lic_plate) REFERENCES vehicles(lic_plate), "
                + "FOREIGN KEY (username) REFERENCES users(username), "
                + " PRIMARY KEY (rental_id))";
        stmt.execute(sql);
        stmt.close();
        con.close();
    }

    public void addNewRental(Rental _rent) throws ClassNotFoundException {
        try {
            Connection con = DB_Connection.getConnection();

            Statement stmt = con.createStatement();

            String insertQuery = "INSERT INTO "
                    + " rentals (username, lic_plate, driv_lic, duration, daily_cost, total_cost, rental_date, is_returned, has_insurance, car_change)"
                    + " VALUES ("
                    + "'" + _rent.getUsername() + "',"
                    + "'" + _rent.getLic_plate() + "',"
                    + "'" + _rent.getDriv_lic() + "',"
                    + "'" + _rent.getDuration() + "',"
                    + "'" + _rent.getDaily_cost() + "',"
                    + "'" + _rent.getTotal_cost() + "',"
                    + "'" + _rent.getRental_date() + "',"
                    + "'" + _rent.Is_returned() + "',"
                    + "'" + _rent.getHas_insurance() + "',"
                    + "'" + _rent.getCar_change() + "'"
                    + ")";
            //stmt.execute(table);
            System.out.println(insertQuery);
            stmt.executeUpdate(insertQuery);
            System.out.println("# The rental was successfully added in the database.");

            stmt.close();

        } catch (SQLException ex) {
            Logger.getLogger(EditRentalTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
