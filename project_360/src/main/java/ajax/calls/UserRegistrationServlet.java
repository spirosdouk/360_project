package ajax.calls;

import database_tables.EditUserTable;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import java.io.IOException;
import java.io.PrintWriter;

import mainClasses.User;

@WebServlet("/UserRegistrationServlet")
public class UserRegistrationServlet extends HttpServlet {
    static User user = new User();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("bro");
        try {
            EditUserTable userTable = new EditUserTable();

            BufferedReader inputJSONfromClient = request.getReader();
            user = userTable.jsonToUser(inputJSONfromClient);

            if(user != null) {
                userTable.addPetUserFromJSON(userTable.userToJSON(user));

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                String jsonString = userTable.userToJSON(user);
                PrintWriter out = response.getWriter();
                response.setStatus(200);
                out.write(jsonString);
            } else {
                response.setStatus(400);
                response.getWriter().write("Invalid JSON format");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            response.getWriter().write("Internal Server Error");
        }
    }
}
