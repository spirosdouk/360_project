package database_tables;

import com.google.gson.Gson;
import database_connect.DB_Connection;
import java.io.BufferedReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import mainClasses.User;

/**
 *
 * @author dimos
 */
public class EditUserTable {
//     public String petKeeperToJSON(PetKeeper user){

    public void addPetUserFromJSON(String json) throws ClassNotFoundException {
        User user = jsonToPetUser(json);
        addNewUser(user);
    }
    public String userToJSON(User user) {
        Gson gson = new Gson();

        String json = gson.toJson(user, User.class);
        return json;
    }
    public User jsonToPetUser(String json) {
        Gson gson = new Gson();

        User user = gson.fromJson(json, User.class);
        return user;
    }

    public User jsonToUser(BufferedReader reader) {
        Gson gson = new Gson();
        return gson.fromJson(reader, User.class);
    }

    public void deleteUser(String id) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String delete = "DELETE FROM users WHERE user_id = '" + id + "'";
        stmt.executeUpdate(delete);
    }

    public ArrayList<User> getAllUsers() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ArrayList<User> tmp = new ArrayList<>();
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM users");
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                User _user = gson.fromJson(json, User.class);
                tmp.add(_user);
            }
            return tmp;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
        }
        return null;
    }

    public void updateUserField(String username, String field, String value) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String update = "UPDATE users SET " + field + "='" + value + "' WHERE username = '" + username + "'";
        stmt.executeUpdate(update);
    }

    public void createUsersTable() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        String query = "CREATE TABLE users "
                + "(user_id INTEGER not NULL AUTO_INCREMENT, "
                + "    username VARCHAR(30) not null unique,"
                + "    password VARCHAR(32) not null,"
                + "    name VARCHAR(50) not null,"
                + "    birthdate DATE not null,"
                + "    address VARCHAR(100) not null,"
                + "    driv_lic INT,"
                + "    credit_card INT not null,"
                + " PRIMARY KEY (user_id))";
        stmt.execute(query);
        stmt.close();
    }

    public void addNewUser(User user) throws ClassNotFoundException {
        try {
            Connection con = DB_Connection.getConnection();

            Statement stmt = con.createStatement();

            String insertQuery = "INSERT INTO "
                    + " users (username,password,name,birthdate,address, driv_lic, credit_card)"
                    + " VALUES ("
                    + "'" + user.getUsername() + "',"
                    + "'" + user.getPassword() + "',"
                    + "'" + user.getName() + "',"
                    + "'" + user.getBirthdate() + "',"
                    + "'" + user.getAddress() + "',"
                    + "'" + user.getDriv_lic() + "',"
                    + "'" + user.getCredit_card() + "'"
                    + ")";
            //stmt.execute(table);
            System.out.println(insertQuery);
            stmt.executeUpdate(insertQuery);
            System.out.println("# The user was successfully added in the database.");

            /* Get the member id from the database and set it to the member */
            stmt.close();

        } catch (SQLException ex) {
            Logger.getLogger(EditUserTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}