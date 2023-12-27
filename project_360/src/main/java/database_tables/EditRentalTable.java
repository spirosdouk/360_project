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
import mainClasses.Rental;

/**
 *
 * @author dimos
 */
public class EditRentalTable {

    public void deleteRental(String id) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String delete = "DELETE FROM retnals WHERE rental_id = '" + id + "'";
        stmt.executeUpdate(delete);
    }

    public ArrayList<Rental> getAllRetnals() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ArrayList<Rental> tmp = new ArrayList<>();
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM retnals");
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

    public void updateRentalField(String username, String field, String value) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String update = "UPDATE retnals SET " + field + "='" + value + "' WHERE username = '" + username + "'";
        stmt.executeUpdate(update);
    }

    public void createRentalTable() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String sql = "CREATE TABLE retnals "
                + "(rental_id INTEGER not NULL AUTO_INCREMENT, "
                + " vehicle_id INTEGER not NULL, "
                + " user_id INTEGER not NULL, "
                + " driv_lic INTEGER not NULL, "
                + " rental_date DATE not NULL, "
                + " duration INTEGER not NULL, "
                + " total_cost INTEGER not NULL, "
                + " is_returned VARCHAR(7) not NULL, "
                + "FOREIGN KEY (vehicle_id) REFERENCES vehicles(vehicle_id), "
                + "FOREIGN KEY (user_id) REFERENCES users(user_id), "
                + " PRIMARY KEY (rental_id))";
        stmt.execute(sql);
        stmt.close();
        con.close();
    }

    public void addNewVehicle(Rental _rent) throws ClassNotFoundException {
        try {
            Connection con = DB_Connection.getConnection();

            Statement stmt = con.createStatement();

            String insertQuery = "INSERT INTO "
                    + " retnals (user_id, vehicle_id, driv_lic, duration, total_cost, rental_date, is_returned)"
                    + " VALUES ("
                    + "'" + _rent.getUser_id() + "',"
                    + "'" + _rent.getVehicle_id() + "',"
                    + "'" + _rent.getDriv_lic() + "',"
                    + "'" + _rent.getRental_date() + "',"
                    + "'" + _rent.getDuration() + "',"
                    + "'" + _rent.getTotal_cost() + "',"
                    + "'" + _rent.Is_returned() + "'"
                    + ")";
            //stmt.execute(table);
            System.out.println(insertQuery);
            stmt.executeUpdate(insertQuery);
            System.out.println("# The rental was successfully added in the database.");

            /* Get the member id from the database and set it to the member */
            stmt.close();

        } catch (SQLException ex) {
            Logger.getLogger(EditRentalTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
