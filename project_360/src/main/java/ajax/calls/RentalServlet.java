/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package ajax.calls;

import database_tables.EditRentalTable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.util.stream.Collectors;

import java.io.IOException;

/**
 *
 * @author spiros
 */
public class RentalServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BufferedReader reader = request.getReader();
        String rentalDataJson = reader.lines().collect(Collectors.joining());
        EditRentalTable rentalTable = new EditRentalTable();

        try {
            rentalTable.addRentalFromJSON(rentalDataJson);
            response.setStatus(HttpServletResponse.SC_OK); // Successful operation
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error occurred while adding new rental");
        }
    }
}
