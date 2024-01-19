/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package ajax.calls;
import java.sql.SQLException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;

import database_tables.EditRentalTable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;




@WebServlet(name = "CheckDriverAvailabilityServlet", urlPatterns = {"/CheckDriverAvailabilityServlet"})
public class CheckDriverAvailabilityServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("broooo");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        int drivingLicence = Integer.parseInt(request.getParameter("driv_lic"));
        String startDateStr = request.getParameter("start_date");
        long duration = Long.parseLong(request.getParameter("duration"));
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd yyyy HH:mm:ss 'GMT'Z (zzzz)", Locale.ENGLISH);
        LocalDate startDate = ZonedDateTime.parse(startDateStr, formatter).toLocalDate();
        
        
        
        
        EditRentalTable rentalTable = new EditRentalTable();
        boolean result;
        try {
            PrintWriter out = response.getWriter();
            result = rentalTable.isDrivingLicenceAvailable(drivingLicence, startDate, duration);
            if (result) {
                out.print("True");
            } else {
                out.print("False");
            }
        } catch (ClassNotFoundException | SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error occurred");
        }
        
    }

}