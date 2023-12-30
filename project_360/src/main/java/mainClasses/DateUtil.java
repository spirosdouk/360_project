/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package mainClasses;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author spiros
 */
public class DateUtil {

    public static Date getCurrentDate() {
        return new Date(); // Current date
    }

    public static Date getDatePlusDays(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }
}
