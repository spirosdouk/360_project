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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
/**
 *
 * @author spiros
 */
public class EditMaintenanceTable {

    public void addMaintenanceFromJSON(String json) throws ClassNotFoundException {
        System.out.println("sucesfully added");
        Maintenance maintenance = jsonToMaintenance(json);
        addNewMaintenance(maintenance);
    }

    public String MaintenanceToJSON(Maintenance maintenance) {
        Gson gson = new Gson();

        String json = gson.toJson(maintenance, Maintenance.class);
        return json;
    }

    public Maintenance jsonToMaintenance(String json) {
        Gson gson = new Gson();

        Maintenance maintenance = gson.fromJson(json, Maintenance.class);
        return maintenance;
    }
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
                + " lic_plate VARCHAR(10) not NULL,"
                + " cost INTEGER not NULL, "
                + " start_date DATE not NULL, "
                + " end_date DATE not NULL, "
                + " maint_type VARCHAR(15) not NULL, "
                + " status VARCHAR(15) not NULL, "
                + "FOREIGN KEY (lic_plate) REFERENCES vehicles(lic_plate), "
                + " PRIMARY KEY (maintenance_id))";
        stmt.execute(sql);
        stmt.close();
        con.close();
    }
    /**
     * Creates a new Maintenance object based on the provided parameters.
     *
     * @param licensePlate The license plate of the vehicle.
     * @param issueType The type of issue (maintenance type).
     * @param damageCost The cost associated with the maintenance.
     * @return A new Maintenance object.
     */
    public Maintenance createMaintenance(String licensePlate, String issueType, double damageCost) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Example date format
        Date startDate = new Date(); // Current date
        String formattedStartDate = dateFormat.format(startDate);

        // Calculate end date based on issue type
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        if("accidentReport".equals(issueType)) {
            calendar.add(Calendar.DATE, 3); // Add 3 days for accident report
        }
        Date endDate = calendar.getTime();
        String formattedEndDate = dateFormat.format(endDate);

        String status = "ongoing"; // Assuming "ongoing" is a default status
        int cost = (int) damageCost; // Convert to int if your constructor expects int

        return new Maintenance(licensePlate, cost, formattedStartDate, formattedEndDate, issueType, status);
    }
    public void addNewMaintenance(Maintenance _mainten) throws ClassNotFoundException {
        try {
            Connection con = DB_Connection.getConnection();

            Statement stmt = con.createStatement();

            String insertQuery = "INSERT INTO "
                    + " maintenance (maintenance_id, lic_plate, cost, start_date, end_date, maint_type, status)"
                    + " VALUES ("
                    + "'" + _mainten.getMaintenance_id() + "',"
                    + "'" + _mainten.getLic_plate() + "',"
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
