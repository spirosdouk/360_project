package ajax.calls;

import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.util.stream.Collectors;
import mainClasses.User;
import database_tables.EditUserTable;

@WebServlet(name = "CheckUsernameServlet", urlPatterns = {"/CheckUsernameServlet"})
public class CheckUsernameServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");

        EditUserTable userTable = new EditUserTable();
        User user = null;

        try {
            user = userTable.getUserByUsername(username);
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try (PrintWriter out = response.getWriter()) {
            if(user != null) {
                Gson gson = new Gson();
                String userJson = gson.toJson(user);
                out.print(userJson);
            } else {
                out.print("{}"); // Empty JSON object
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Read and parse the request data
        BufferedReader reader = request.getReader();
        String requestData = reader.lines().collect(Collectors.joining());
        Gson gson = new Gson();
        User loginUser = gson.fromJson(requestData, User.class);

        EditUserTable userTable = new EditUserTable();
        User authenticatedUser = null;

        try {
            // Check if the user exists with the provided username and password
            authenticatedUser = userTable.getUserByUsernameAndPassword(loginUser.getUsername(), loginUser.getPassword());
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try (PrintWriter out = response.getWriter()) {
            if(authenticatedUser != null) {
                out.print(gson.toJson(authenticatedUser)); // User found
            } else {
                out.print("{}"); // No user found
            }
        }
    }
}
