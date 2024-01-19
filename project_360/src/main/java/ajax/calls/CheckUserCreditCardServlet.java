
package ajax.calls;

import database_tables.EditUserTable;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mainClasses.User;


@WebServlet(name = "CheckUserCreditCardServlet", urlPatterns = {"/CheckUserCreditCardServlet"})
public class CheckUserCreditCardServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String username = request.getParameter("username");

        EditUserTable userTable = new EditUserTable();
        User user;

        try {
            user = userTable.getUserByUsername(username);
            PrintWriter out = response.getWriter();
            
            BigDecimal creditCardNumber = user.getCredit_card();
            if (!creditCardNumber.equals(0)){
                String firstThreeDigits = String.valueOf(creditCardNumber).substring(0, 3);
                String lastThreeDigits = String.valueOf(creditCardNumber).substring(13);
                
                out.print("True, first_three_digits: " + firstThreeDigits + 
                        ", last_three_digits: " + lastThreeDigits);
            } else {
                out.print("False");
            }
        } catch (IOException | ClassNotFoundException | SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error occurred");
        }

    }


}
