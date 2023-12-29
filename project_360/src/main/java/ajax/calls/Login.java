///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package ajax.calls;
//
//import com.google.gson.Gson;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import java.io.IOException;
//import java.sql.SQLException;
//import java.util.HashMap;
//import java.util.Map;
//import java.io.PrintWriter;
//import com.google.gson.JsonObject;
//
//import mainClasses.PetKeeper;
//import database.tables.EditPetKeepersTable;
//import java.io.BufferedReader;
//
///**
// *
// * @author spiros
// */
//public class Login extends HttpServlet {
//
//    @Override
//    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//
//        EditPetKeepersTable petKeepersTable = new EditPetKeepersTable();
//        HttpSession session = request.getSession();
//        String loggedInUser = (String) session.getAttribute("loggedIn");
//
//        BufferedReader reader = request.getReader();
//        StringBuilder sb = new StringBuilder();
//        String line;
//        while ((line = reader.readLine()) != null) {
//            sb.append(line);
//        }
//
//        Gson gson = new Gson();
//        Map<String, String> updatedData = gson.fromJson(sb.toString(), HashMap.class);
//
//        boolean updateSuccessful = petKeepersTable.updateFields(loggedInUser, updatedData);
//
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//        PrintWriter out = response.getWriter();
//        JsonObject jsonResponse = new JsonObject();
//        jsonResponse.addProperty("success", updateSuccessful);
//        out.print(jsonResponse.toString());
//        out.flush();
//    }
//
//    /**
//     * Handles the HTTP <code>GET</code> method.
//     *
//     * @param request servlet request
//     * @param response servlet response
//     * @throws ServletException if a servlet-specific error occurs
//     * @throws IOException if an I/O error occurs
//     */
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        HttpSession session = request.getSession();
//        String loggedInUser = (String) session.getAttribute("loggedIn");
//
//        if (loggedInUser != null) {
//            EditPetKeepersTable petKeepersTable = new EditPetKeepersTable();
//
//            try {
//                PetKeeper user = petKeepersTable.getPetKeeperByUsername(loggedInUser);
//
//                if (user != null) {
//                    Map<String, Object> userData = new HashMap<>();
//                    userData.put("Personalpage", user.getPersonalpage());
//                    userData.put("Email", user.getEmail());
//                    userData.put("Username", user.getUsername());
//                    userData.put("Password", user.getPassword());
//                    userData.put("Firstname", user.getFirstname());
//                    userData.put("Lastname", user.getLastname());
//                    userData.put("Birthdate", user.getBirthdate());
//                    userData.put("Gender", user.getGender());
//                    userData.put("Country", user.getCountry());
//                    userData.put("City", user.getCity());
//                    userData.put("Address", user.getAddress());
//                    userData.put("Job", user.getJob());
//                    userData.put("Telephone", user.getTelephone());
//                    userData.put("Property", user.getProperty());
//                    userData.put("Propertydescription", user.getPropertydescription());
//                    userData.put("Catkeeper", user.getCatkeeper());
//                    userData.put("Dogkeeper", user.getDogkeeper());
//                    userData.put("Catprice", user.getCatprice());
//                    userData.put("Dogprice", user.getDogprice());
//
//                    String json = new Gson().toJson(userData);
//
//                    response.setContentType("application/json");
//                    response.setCharacterEncoding("UTF-8");
//                    response.getWriter().write(json);
//                } else {
//                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
//                }
//            } catch (SQLException | ClassNotFoundException e) {
//                e.printStackTrace();
//                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            }
//
//        } else {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        }
//    }
//
//    /**
//     * Handles the HTTP <code>POST</code> method.
//     *
//     * @param request servlet request
//     * @param response servlet response
//     * @throws ServletException if a servlet-specific error occurs
//     * @throws IOException if an I/O error occurs
//     */
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//
//        BufferedReader reader = request.getReader();
//        StringBuilder sb = new StringBuilder();
//        String line;
//        while ((line = reader.readLine()) != null) {
//            sb.append(line);
//        }
//        String requestData = sb.toString();
//
//        Gson gson = new Gson();
//        PetKeeper petKeeperRequest = gson.fromJson(requestData, PetKeeper.class);
//
//        HttpSession session = request.getSession();
//
//        try {
//            if (doesUserExist(petKeeperRequest.getUsername(), petKeeperRequest.getEmail())) {
//                session.setAttribute("loggedIn", petKeeperRequest.getUsername());
//
//                session.setMaxInactiveInterval(30 * 60);
//
//                int activeUsers = 0;
//                if (request.getServletContext().getAttribute("activeUsers") != null) {
//                    activeUsers = (int) request.getServletContext().getAttribute("activeUsers");
//                }
//                request.getServletContext().setAttribute("activeUsers", activeUsers + 1);
//                response.setStatus(200);
//                response.getWriter().write("Login successful");
//
//            } else {
//                response.setStatus(403);
//                response.getWriter().write("Invalid username or password");
//
//            }
//        } catch (SQLException | ClassNotFoundException e) {
//            e.printStackTrace();
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    private boolean doesUserExist(String username, String password) throws SQLException, ClassNotFoundException {
//        EditPetKeepersTable editPetKeepersTable = new EditPetKeepersTable();
//        PetKeeper user = editPetKeepersTable.databaseToPetKeepers(username, password);
//        return user != null;
//    }
//}
