package ajax.calls;
import java.sql.SQLException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import database_tables.EditMaintenanceTable;
import database_tables.EditVehicleTable;
import database_tables.EditUserTable;
import database_tables.EditRentalTable;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import mainClasses.Maintenance;
import mainClasses.Vehicle;
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
        EditVehicleTable vehicleTable = new EditVehicleTable();

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
                totalCost += extraCharge;
                System.out.println("extra charge: $" + extraCharge);
            }

            rentalTable.updateRentalReturnStatus(licPlate, totalCost, "true");
            vehicleTable.updateVehicleRentalStatus(licPlate, false);

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

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StringBuilder sb = new StringBuilder();
        String line;

        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing return");
            return;
        }

        try {
            JSONObject jsonObject = new JSONObject(sb.toString());
            String licensePlate = jsonObject.optString("licensePlate");
            String issueType = jsonObject.optString("issueType");
            String insurance = jsonObject.optString("hasInsurance");
            double totalCost = jsonObject.optDouble("totalCost");
            int daily_cost = jsonObject.optInt("daily_cost");
            String username = jsonObject.optString("username");
            String rentalDate = jsonObject.optString("rental_date");
            int duration = jsonObject.optInt("duration");

            System.out.println(insurance);
            System.out.println(issueType);
            System.out.println(licensePlate);
            System.out.println(totalCost);
            System.out.println(daily_cost);
            System.out.println(username);
            System.out.println(rentalDate);
            System.out.println(duration);

            EditRentalTable rentalTable = new EditRentalTable();
            EditMaintenanceTable maintanace = new EditMaintenanceTable();
            EditVehicleTable vehicleTable = new EditVehicleTable();
            EditUserTable userTable = new EditUserTable();

            JSONObject responseJson = new JSONObject();
            Vehicle assignedVehicle = null;

            if("accidentReport".equals(issueType)) {

                if("true".equals(insurance)) {
                    double damagecost = daily_cost * 20;
                    rentalTable.updateRentalReturnStatus(licensePlate, totalCost, "true");

                    Maintenance newMaintenance = new Maintenance(licensePlate, "maint");
                    maintanace.addNewMaintenance(newMaintenance);

                    String carChange = "none";//licence plate
                    int drivingLicence = userTable.getUserDrivingLicence(username);
                    assignedVehicle = vehicleTable.assignNewVehicle(newMaintenance.getLic_plate(), username, drivingLicence, duration,
                            0, rentalDate, "false", insurance, carChange);

                    vehicleTable.updateVehicleRentalStatus(licensePlate, false);
                    vehicleTable.updateDamageStatus(licensePlate, true);
                } else {
                    double chargeAmount = totalCost * 3; // Charge triple the total cost
                    rentalTable.updateRentalReturnStatus(licensePlate, chargeAmount, "true");

                    Maintenance newMaintenance = new Maintenance(licensePlate, "maint");
                    maintanace.addNewMaintenance(newMaintenance);

                    String carChange = "none";//licence plate
                    int drivingLicence = userTable.getUserDrivingLicence(username);
                    assignedVehicle = vehicleTable.assignNewVehicle(newMaintenance.getLic_plate(), username, drivingLicence, duration,
                            0, rentalDate, "false", insurance, carChange);

                    vehicleTable.updateVehicleRentalStatus(licensePlate, false);
                    vehicleTable.updateDamageStatus(licensePlate, true);
                    responseJson.put("chargeAmount", chargeAmount);
                }
            } else if("vehicleDamage".equals(issueType)) {
                rentalTable.updateRentalReturnStatus(licensePlate, totalCost, "true");

                Maintenance newMaintenance = new Maintenance(licensePlate, "repair");
                maintanace.addNewMaintenance(newMaintenance);

                String carChange = "none";//licence plate
                int drivingLicence = userTable.getUserDrivingLicence(username);
                assignedVehicle = vehicleTable.assignNewVehicle(newMaintenance.getLic_plate(), username, drivingLicence, duration,
                        0, rentalDate, "false", insurance, carChange);

                vehicleTable.updateVehicleRentalStatus(licensePlate, false);
                vehicleTable.updateDamageStatus(licensePlate, true);
            }
            if(assignedVehicle != null) {
                rentalTable.updateCarChange(licensePlate, assignedVehicle.getLic_plate());
                responseJson.put("newVehicleLicensePlate", assignedVehicle.getLic_plate());
            }
            responseJson.put("status", "success");

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(responseJson.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JSON data");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred");
        }
    }

}
