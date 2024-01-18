/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package ajax.calls;

import database_tables.EditVehicleTable;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mainClasses.Vehicle;

/**
 *
 * @author dimos
 */
public class AddVehicleAdmin extends HttpServlet {

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
            out.println("<title>Servlet AddVehicleAdmin</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AddVehicleAdmin at " + request.getContextPath() + "</h1>");
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
        String lic_plate = request.getParameter("lic_plate");

        EditVehicleTable evt = new EditVehicleTable();
        ArrayList<Vehicle> list;
        try {
            list = evt.getAllVehicles();
            for (Vehicle vec : list) {
                if (vec.getLic_plate().equals(lic_plate)) {
                    response.setStatus(402);
                    return;
                }
            }
            response.setStatus(200);
        } catch (SQLException ex) {
            Logger.getLogger(AddVehicleAdmin.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AddVehicleAdmin.class.getName()).log(Level.SEVERE, null, ex);
        }

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
        String sub = request.getParameter("sub");
        String brand = request.getParameter("brand");
        String model = request.getParameter("model");
        String color = request.getParameter("color");
        String lic_plate = request.getParameter("lic_plate");
        String type = request.getParameter("type");
        int km = Integer.parseInt(request.getParameter("km_range"));
        int drc = Integer.parseInt(request.getParameter("drc"));
        int dic = Integer.parseInt(request.getParameter("dic"));

        EditVehicleTable evt = new EditVehicleTable();
        Vehicle veh = new Vehicle(brand, model, color, type, lic_plate, km, drc, dic, "false", sub, "false");
        try {
            evt.addNewVehicle(veh);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AddVehicleAdmin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
