package mainClasses;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author spiros
 */
public class DateUtil {

    public static Date getCurrentDate() {
        return new Date();
    }

    public static Date getDatePlusDays(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }
}
