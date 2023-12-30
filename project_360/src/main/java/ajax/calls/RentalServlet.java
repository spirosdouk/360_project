package ajax.calls;

import database_tables.EditRentalTable;

import java.util.stream.Collectors;
import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.io.BufferedReader;
import java.util.ArrayList;
import mainClasses.Rental;

import java.io.IOException;

/**
 *
 * @author spiros
 */
public class RentalServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        EditRentalTable rentalTable = new EditRentalTable();
        ArrayList<Rental> userRentals;

        try {
            userRentals = rentalTable.getRentalsByUsername(username);
            Gson gson = new Gson();
            String rentalsJson = gson.toJson(userRentals);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(rentalsJson);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BufferedReader reader = request.getReader();
        String rentalDataJson = reader.lines().collect(Collectors.joining());
        EditRentalTable rentalTable = new EditRentalTable();

        try {
            rentalTable.addRentalFromJSON(rentalDataJson);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error occurred while adding new rental");
        }
    }

}
