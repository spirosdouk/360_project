/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package ajax.calls;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import database_tables.EditRentalTable;

/**
 *
 * @author spiros
 */
@WebServlet(name = "ReturnRentalServlet", urlPatterns = {"/ReturnRentalServlet"})
public class ReturnRentalServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BufferedReader reader = request.getReader();
        String requestData = reader.lines().collect(Collectors.joining());
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(requestData, JsonObject.class);
        EditRentalTable rentalTable = new EditRentalTable();

        String licPlate = jsonObject.get("lic_plate").getAsString();
        double totalCost = jsonObject.get("total_cost").getAsDouble();
        String rentalDateStr = jsonObject.get("rental_date").getAsString();
        long duration = jsonObject.get("duration").getAsLong();

        try {
            LocalDate rentalDate = LocalDate.parse(rentalDateStr);
            LocalDate dueDate = rentalDate.plusDays(duration);
            LocalDate currentDate = LocalDate.now();

            long extraDays = ChronoUnit.DAYS.between(dueDate, currentDate);
            long extraHours = ChronoUnit.HOURS.between(dueDate.atStartOfDay(), currentDate.atStartOfDay());

            System.out.println("extra days: " + extraDays);
            System.out.println("extra hours: " + extraHours);

            if(extraDays > 0 || extraHours > 0) {
                double extraCharge = (extraDays * 24 + extraHours) * 1.0; // 1 dollar per extra hour
                totalCost += extraCharge * 10;
                System.out.println("extra charge: $" + extraCharge);
            }


            // Update the rental record in the database
            rentalTable.updateRentalReturnStatus(licPlate, totalCost, "true");

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            JsonObject responseJson = new JsonObject();
            responseJson.addProperty("message", "Rental returned successfully with total cost: " + totalCost);
            out.print(gson.toJson(responseJson));

            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing return");
        }
    }
}
