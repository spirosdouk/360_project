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
import mainClasses.Subtype;

/**
 *
 * @author dimos
 */
public class EditSubtypeTable {
    public void deleteSubtype(String id) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String delete = "DELETE FROM subtype WHERE vehicle_id = '" + id + "'";
        stmt.executeUpdate(delete);
    }

    public ArrayList<Subtype> getAllSubtypes() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ArrayList<Subtype> tmp = new ArrayList<>();
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM subtype");
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                Subtype _subtype = gson.fromJson(json, Subtype.class);
                tmp.add(_subtype);
            }
            return tmp;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
        }
        return null;
    }

    public void updateSubtypeField(String username, String field, String value) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String update = "UPDATE subtype SET " + field + "='" + value + "' WHERE username = '" + username + "'";
        stmt.executeUpdate(update);
    }

    public void createSubtypesTable() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        String query = "CREATE TABLE subtype "
                + "(subtype_name VARCHAR(30) not null unique,"
                + "    capacity INTEGER not null,"
                + " PRIMARY KEY (subtype_name))";
        stmt.execute(query);
        stmt.close();
    }

    public void addNewSubtype(Subtype _subtype) throws ClassNotFoundException {
        try {
            Connection con = DB_Connection.getConnection();

            Statement stmt = con.createStatement();

            String insertQuery = "INSERT INTO "
                    + " subtype (subtype_name, capacity)"
                    + " VALUES ("
                    + "'" + _subtype.getSubtype_name() + "',"
                    + "'" + _subtype.getCapacity() + "'"
                    + ")";
            //stmt.execute(table);
            System.out.println(insertQuery);
            stmt.executeUpdate(insertQuery);
            System.out.println("# The subtype was successfully added in the database.");

            /* Get the member id from the database and set it to the member */
            stmt.close();

        } catch (SQLException ex) {
            Logger.getLogger(EditVehicleTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
