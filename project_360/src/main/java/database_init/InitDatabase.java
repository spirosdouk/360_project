/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database_init;

import static database_connect.DB_Connection.getInitialConnection;

import database_tables.EditUserTable;
import database_tables.EditVehicleTable;
import database_tables.EditRentalTable;
import database_tables.EditMaintenanceTable;
import database_tables.EditSubtypeTable;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author dimos
 */
public class InitDatabase {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        InitDatabase init = new InitDatabase();
        init.dropDatabase();
        init.initDatabase();
        init.initTables();
        init.addToDatabaseExamples();
        init.getFromDatabase();
        init.updateRecords();
        init.deleteFromDatabase();
    }

    public void dropDatabase() throws SQLException, ClassNotFoundException {
        Connection conn = getInitialConnection();
        Statement stmt = conn.createStatement();
        String sql = "DROP DATABASE IF EXISTS HY360_team18";
        stmt.executeUpdate(sql);
        System.out.println("Database dropped successfully...");
    }

    public void initDatabase() throws SQLException, ClassNotFoundException {
        Connection conn = getInitialConnection();
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE DATABASE IF NOT EXISTS HY360_team18");
        stmt.close();
        conn.close();
    }

    public void initTables() throws SQLException, ClassNotFoundException {
        EditUserTable eut = new EditUserTable();
        eut.createUsersTable();

        EditSubtypeTable est = new EditSubtypeTable();
        est.createSubtypesTable();

        EditVehicleTable evt = new EditVehicleTable();
        evt.createVehicleTable();

        EditRentalTable ert = new EditRentalTable();
        ert.createRentalTable();

        EditMaintenanceTable emt = new EditMaintenanceTable();
        emt.createMaintenanceTable();
    }

    public void addToDatabaseExamples() throws ClassNotFoundException, SQLException {
        EditUserTable eut = new EditUserTable();
        eut.addNewUser(Resources.user0);
        eut.addNewUser(Resources.user1);
        eut.addNewUser(Resources.user2);
        eut.addNewUser(Resources.user3);
        eut.addNewUser(Resources.user4);
        eut.addNewUser(Resources.user5);

        EditSubtypeTable est = new EditSubtypeTable();
        est.addNewSubtype(Resources.st);
        est.addNewSubtype(Resources.st0);
        est.addNewSubtype(Resources.st1);
        est.addNewSubtype(Resources.st2);
        est.addNewSubtype(Resources.st3);
        est.addNewSubtype(Resources.st4);

        EditVehicleTable evt = new EditVehicleTable();
        evt.addNewVehicle(Resources.veh0);
        evt.addNewVehicle(Resources.veh1);
        evt.addNewVehicle(Resources.veh2);
        evt.addNewVehicle(Resources.veh3);
        evt.addNewVehicle(Resources.veh4);
        evt.addNewVehicle(Resources.veh5);

        EditRentalTable ert = new EditRentalTable();
        ert.addNewRental(Resources.rental0);
        ert.addNewRental(Resources.rental1);
        ert.addNewRental(Resources.rental2);
        ert.addNewRental(Resources.rental3);
        ert.addNewRental(Resources.rental4);

        EditMaintenanceTable emt = new EditMaintenanceTable();
        emt.addNewMaintenance(Resources.maint0);
        emt.addNewMaintenance(Resources.maint1);
        emt.addNewMaintenance(Resources.maint2);
        emt.addNewMaintenance(Resources.maint3);
        emt.addNewMaintenance(Resources.maint4);
    }

    public void getFromDatabase() throws ClassNotFoundException, SQLException {
        EditUserTable eut = new EditUserTable();
        System.out.println(eut.getAllUsers());
    }

    public void updateRecords() throws ClassNotFoundException, SQLException {
        EditUserTable eut = new EditUserTable();
        eut.updateUserField("mitsos", "password", "aaa111!");
    }

    public void deleteFromDatabase() throws ClassNotFoundException, SQLException {
        EditUserTable eut = new EditUserTable();
        eut.deleteUser("6");
    }
}
