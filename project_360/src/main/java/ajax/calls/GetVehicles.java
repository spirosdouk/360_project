/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package ajax.calls;

import mainClasses.Vehicle;
import database_tables.EditVehicleTable;
import java.sql.SQLException;
import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.annotation.WebServlet;

/**
 *
 * @author spiros
 */
@WebServlet(name = "GetVehicles", urlPatterns = {"/GetVehiclesServlet"})

public class GetVehicles extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String vehicleType = request.getParameter("vehicleType");

        EditVehicleTable rentalTable = new EditVehicleTable();
        ArrayList<Vehicle> rentals;

        try {
            rentals = rentalTable.getAvailableVehiclesByType(vehicleType);
            Gson gson = new Gson();
            String rentalsJson = gson.toJson(rentals);
            PrintWriter out = response.getWriter();
            out.print(rentalsJson);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error occurred");
        }
    }
}

