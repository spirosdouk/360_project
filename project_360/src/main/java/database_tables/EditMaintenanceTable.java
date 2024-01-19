package database_tables;

import com.google.gson.Gson;
import database_connect.DB_Connection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import mainClasses.Maintenance;
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

    public void updateMaintenanceField(String maintenance_id, String field, String value) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String update = "UPDATE maintenance SET " + field + "='" + value + "' WHERE maintenance_id = '" + maintenance_id + "'";
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

    public String getVehicleYearlyMaintCost() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs;
        String fields = "MaintenanceYear, M.maint_type, TotalCost";
        String query = "SELECT YEAR(M.start_date) AS MaintenanceYear, M.maint_type, SUM(M.cost) AS TotalCost FROM maintenance M GROUP BY YEAR(M.start_date), M.maint_type;";
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

    public void checkMaintenance() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        PreparedStatement selectStmt = null;
        PreparedStatement updateStmt = null;
        ResultSet rs = null;

        try {
            // SQL to find maintenance records where the end date has passed
            String selectQ = "SELECT maintenance_id FROM maintenance WHERE end_date <= CURRENT_DATE()";

            // Prepare the SELECT statement
            selectStmt = con.prepareStatement(selectQ);
            rs = selectStmt.executeQuery();

            // SQL to update the status of maintenance records
            String updateQ = "UPDATE maintenance SET status = 'finished' WHERE maintenance_id = ?";

            // Prepare the UPDATE statement
            updateStmt = con.prepareStatement(updateQ);

            // Iterate through the results and update each record
            while (rs.next()) {
                int maintenanceId = rs.getInt("maintenance_id");

                updateStmt.setInt(1, maintenanceId);
                updateStmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
