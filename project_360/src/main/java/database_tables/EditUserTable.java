package database_tables;

import com.google.gson.Gson;
import database_connect.DB_Connection;
import java.io.BufferedReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
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

    public void addUserFromJSON(String json) throws ClassNotFoundException {
        User user = jsonToUser(json);
        addNewUser(user);
    }
    public String userToJSON(User user) {
        Gson gson = new Gson();

        String json = gson.toJson(user, User.class);
        return json;
    }
    public User jsonToUser(String json) {
        Gson gson = new Gson();

        User user = gson.fromJson(json, User.class);
        return user;
    }

    public User jsonToUser(BufferedReader reader) {
        Gson gson = new Gson();
        return gson.fromJson(reader, User.class);
    }

    public void deleteUser(String username) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String delete = "DELETE FROM users WHERE username = '" + username + "'";
        stmt.executeUpdate(delete);
    }
    public User getUserByUsername(String username) throws SQLException, ClassNotFoundException {
        User user = null;
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            con = DB_Connection.getConnection();
            stmt = con.createStatement();

            String query = "SELECT * FROM users WHERE username = '" + username + "'";
            rs = stmt.executeQuery(query);

            if(rs.next()) {
                user = new User();
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password")); // Assuming there is a password column
                user.setName(rs.getString("name"));
                user.setAddress(rs.getString("address"));
                user.setBirthdate(rs.getString("birthdate")); // Make sure the format matches
                user.setDriv_lic(rs.getInt("driv_lic"));
                user.setCredit_card(rs.getBigDecimal("credit_card"));
                // Set other user properties as needed
            }
        } catch (SQLException ex) {
            Logger.getLogger(EditUserTable.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if(rs != null) {
                rs.close();
            }
            if(stmt != null) {
                stmt.close();
            }
            if(con != null) {
                con.close();
            }
        }

        return user;
    }

    public User getUserByUsernameAndPassword(String username, String password) throws SQLException, ClassNotFoundException {
        User user = null;
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            con = DB_Connection.getConnection();
            stmt = con.createStatement();

            // Use parameter placeholders to prevent SQL injection
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, username);
            pstmt.setString(2, password); // Assuming you store hashed passwords
            rs = pstmt.executeQuery();

            if(rs.next()) {
                user = new User();
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setName(rs.getString("name"));
                user.setAddress(rs.getString("address"));
                user.setBirthdate(rs.getString("birthdate"));
                user.setDriv_lic(rs.getInt("driv_lic"));
                user.setCredit_card(rs.getBigDecimal("credit_card"));
                // Set other user properties as needed
            }
        } catch (SQLException ex) {
            Logger.getLogger(EditUserTable.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if(rs != null) {
                rs.close();
            }
            if(stmt != null) {
                stmt.close();
            }
            if(con != null) {
                con.close();
            }
        }

        return user;
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
    public int getUserDrivingLicence(String username) throws SQLException, ClassNotFoundException {
        EditUserTable userTable = new EditUserTable();
        User user = userTable.getUserByUsername(username);

        if(user != null) {
            return (int) user.getDriv_lic(); // Assuming getDriv_lic() method exists in your User class
        } else {
            // Handle the case where the user is not found
            throw new SQLException("User not found");
        }
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
                + "(username VARCHAR(30) not null unique,"
                + "    password VARCHAR(32) not null,"
                + "    name VARCHAR(50) not null,"
                + "    birthdate DATE not null,"
                + "    address VARCHAR(100) not null,"
                + "    driv_lic BIGINT,"
                + "    credit_card BIGINT not null,"
                + " PRIMARY KEY (username))";
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
