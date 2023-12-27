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
import mainClasses.Maintenance;

/**
 *
 * @author dimos
 */
public class EditMaintenanceTable {
    public void deleteMaintenance(String id) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String delete = "DELETE FROM maintenance WHERE maintenance_id = '" + id + "'";
        stmt.executeUpdate(delete);
    }

    public ArrayList<Maintenance> getAllmaintenances() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ArrayList<Maintenance> tmp = new ArrayList<>();
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM maintenance");
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                Maintenance _mainten = gson.fromJson(json, Maintenance.class);
                tmp.add(_mainten);
            }
            return tmp;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
        }
        return null;
    }

    public void updateMaintenanceField(String username, String field, String value) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String update = "UPDATE maintenance SET " + field + "='" + value + "' WHERE username = '" + username + "'";
        stmt.executeUpdate(update);
    }

    public void createMaintenanceTable() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String sql = "CREATE TABLE maintenance "
                + "(maintenance_id INTEGER not NULL AUTO_INCREMENT, "
                + " vehicle_id INTEGER not NULL, "
                + " cost INTEGER not NULL, "
                + " start_date DATE not NULL, "
                + " end_date DATE not NULL, "
                + " maint_type VARCHAR(15) not NULL, "
                + " status VARCHAR(7) not NULL, "
                + "FOREIGN KEY (vehicle_id) REFERENCES vehicletable(vehicle_id), "
                + " PRIMARY KEY (maintenance_id))";
        stmt.execute(sql);
        stmt.close();
        con.close();
    }

    public void addNewMaintenance(Maintenance _mainten) throws ClassNotFoundException {
        try {
            Connection con = DB_Connection.getConnection();

            Statement stmt = con.createStatement();

            String insertQuery = "INSERT INTO "
                    + " maintenance (maintenance_id, vehicle_id, cost, start_date, end_date, maint_type, status)"
                    + " VALUES ("
                    + "'" + _mainten.getMaintenance_id() + "',"
                    + "'" + _mainten.getVehicle_id() + "',"
                    + "'" + _mainten.getCost() + "',"
                    + "'" + _mainten.getStart_date() + "',"
                    + "'" + _mainten.getEnd_date() + "',"
                    + "'" + _mainten.getMaint_type() + "',"
                    + "'" + _mainten.isStatus() + "'"
                    + ")";
            //stmt.execute(table);
            System.out.println(insertQuery);
            stmt.executeUpdate(insertQuery);
            System.out.println("# The maintenance was successfully added in the database.");

            /* Get the member id from the database and set it to the member */
            stmt.close();

        } catch (SQLException ex) {
            Logger.getLogger(EditRentalTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
