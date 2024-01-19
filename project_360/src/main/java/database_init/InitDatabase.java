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
        evt.addNewVehicle(Resources.veh6);
        evt.addNewVehicle(Resources.veh7);
        evt.addNewVehicle(Resources.veh8);
        evt.addNewVehicle(Resources.veh9);
        evt.addNewVehicle(Resources.veh10);
        evt.addNewVehicle(Resources.veh11);
        evt.addNewVehicle(Resources.veh12);
        evt.addNewVehicle(Resources.veh13);
        evt.addNewVehicle(Resources.veh14);
        evt.addNewVehicle(Resources.veh15);
        evt.addNewVehicle(Resources.veh16);

        EditRentalTable ert = new EditRentalTable();
        ert.addNewRental(Resources.rental0);
        evt.updateVehicleRentalStatus_date(Resources.rental0.getLic_plate(), true, Resources.rental0.getDuration());
        evt.updateVehicleRentalStatus(Resources.rental0.getLic_plate(), false);
        ert.addNewRental(Resources.rental1);
        evt.updateVehicleRentalStatus_date(Resources.rental1.getLic_plate(), true, Resources.rental1.getDuration());
        evt.updateVehicleRentalStatus(Resources.rental1.getLic_plate(), false);
        ert.addNewRental(Resources.rental2);
        evt.updateVehicleRentalStatus_date(Resources.rental2.getLic_plate(), true, Resources.rental2.getDuration());
        evt.updateVehicleRentalStatus(Resources.rental2.getLic_plate(), false);
        ert.addNewRental(Resources.rental3);
        evt.updateVehicleRentalStatus_date(Resources.rental3.getLic_plate(), true, Resources.rental3.getDuration());
        evt.updateVehicleRentalStatus(Resources.rental3.getLic_plate(), false);
        ert.addNewRental(Resources.rental4);
        evt.updateVehicleRentalStatus_date(Resources.rental4.getLic_plate(), true, Resources.rental4.getDuration());
        evt.updateVehicleRentalStatus(Resources.rental4.getLic_plate(), false);
        ert.addNewRental(Resources.rental5);
        evt.updateVehicleRentalStatus_date(Resources.rental5.getLic_plate(), true, Resources.rental5.getDuration());
        evt.updateVehicleRentalStatus(Resources.rental5.getLic_plate(), false);


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
        eut.deleteUser("dasdsadsai");
    }
}
