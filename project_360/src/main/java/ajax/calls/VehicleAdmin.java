/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package ajax.calls;

import database_tables.EditMaintenanceTable;
import database_tables.EditVehicleTable;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mainClasses.Maintenance;


/**
 *
 * @author dimos
 */
public class VehicleAdmin extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet VehicleAdmin</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet VehicleAdmin at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        EditVehicleTable evt = new EditVehicleTable();
        EditMaintenanceTable emt = new EditMaintenanceTable();

        try {
            emt.checkMaintenance();
        } catch (SQLException ex) {
            Logger.getLogger(VehicleAdmin.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(VehicleAdmin.class.getName()).log(Level.SEVERE, null, ex);
        }

        String temp;
        try (PrintWriter out = response.getWriter()) {
            temp = evt.getVehicleFieldForAdmin();
            System.out.println(temp);
            response.setStatus(200);
            out.println(temp);
            return;
        } catch (SQLException ex) {
            Logger.getLogger(VehicleAdmin.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(VehicleAdmin.class.getName()).log(Level.SEVERE, null, ex);
        }
        response.setStatus(500);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String lic_plate = request.getParameter("lic_plate");
        String status = request.getParameter("status");

        System.out.println(lic_plate);
        System.out.println(status);

        EditVehicleTable evt = new EditVehicleTable();
        EditMaintenanceTable emt = new EditMaintenanceTable();

        if (lic_plate != null && status != null) {
            if (status.equals("release")) {
                System.out.println("Here");
                try {
                    evt.deleteVehicle(lic_plate);
                    response.setStatus(200);
                    return;
                } catch (SQLException ex) {
                    Logger.getLogger(VehicleAdmin.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(VehicleAdmin.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (status.equals("maint")) {
                try {
                    Maintenance maint = new Maintenance(lic_plate, status);
                    emt.addNewMaintenance(maint);
                    response.setStatus(200);
                    return;
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(VehicleAdmin.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (status.equals("repair")) {
                try {
                    Maintenance maint = new Maintenance(lic_plate, status);
                    emt.addNewMaintenance(maint);
                    response.setStatus(200);
                    return;
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(VehicleAdmin.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            response.setStatus(500);
        }
    }

}
