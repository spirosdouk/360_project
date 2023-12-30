package database_init;

import mainClasses.Maintenance;
import mainClasses.Rental;
import mainClasses.Subtype;
import mainClasses.User;
import mainClasses.Vehicle;

/**
 *
 * @author dimos
 */
public class Resources {

    static User user0 = new User("mitsos", "letsee!", "Mitsos Mitsakis", "Kalokairinou 112, Heraklion", "2002-01-21", 995566777, 9);
    static User user1 = new User("nina", "aaa111!", "Katerina Nina", "Kalokairinou 112, Heraklion", "2007-01-21", 0, 8);
    static User user2 = new User("mpampis", "aaa111!", "Mpampis", "Kalokairinou 112, Heraklion", "1997-02-15", 112233445, 7);
    static User user3 = new User("tsitsip", "aaa111!", "Tsitsipas", "Kalokairinou 112, Heraklion", "2000-01-21", 153248697, 5);
    static User user4 = new User("sakari", "aaa111!", "Maria Sakari", "Kalokairinou 112, Heraklion", "2001-05-21", 147852369, 4);
    static User user5 = new User("dasdsadsai", "dasdasdasd", "Madasdasdasi", "Kdasdasdasdasdasderaklion", "2001-05-21", 0, 4);


    static Vehicle veh0 = new Vehicle("Fiat", "Panda", "blue", "car", "AD4152", 320, 30, 15, "false", "city car", "true");
    static Vehicle veh1 = new Vehicle("Rad Power", "RadExpand", "black", "bicycle", "b1", 70, 15, 8, "false", "", "false");
    static Vehicle veh2 = new Vehicle("Yamaha", "Bolt", "gray", "bike", "JD4522", 180, 25, 12, "false", "", "false");
    static Vehicle veh3 = new Vehicle("Glion", "Balto", "silver", "scooter", "s1", 320, 30, 15, "false", "", "true");
    static Vehicle veh4 = new Vehicle("Kia", "Sportage", "gray", "car", "TH5263", 320, 30, 15, "true", "SUV", "false");
    static Vehicle veh5 = new Vehicle("Kia", "Sportage", "red", "car", "FK5161", 320, 30, 15, "false", "SUV", "false");

    static Subtype st = new Subtype("NULL", 0);
    static Subtype st0 = new Subtype("SUV", 7);
    static Subtype st1 = new Subtype("city car", 5);
    static Subtype st2 = new Subtype("Super Car", 2);
    static Subtype st3 = new Subtype("Van", 9);
    static Subtype st4 = new Subtype("Cabrio", 4);

    static Maintenance maint0 = new Maintenance("AD4152", 50, "2023-05-05", "2023-05-06", "maintenance", "finished");
    static Maintenance maint1 = new Maintenance("b1", 50, "2023-06-05", "2023-06-06", "maintenance", "finished");
    static Maintenance maint2 = new Maintenance("JD4522", 150, "2023-08-15", "2023-08-18", "repair", "finished");
    static Maintenance maint3 = new Maintenance("AD4152", 50, "2023-12-05", "2023-12-06", "maintenance", "finished");
    static Maintenance maint4 = new Maintenance("FK5161", 250, "2024-01-18", "2024-01-21", "repair", "ongoing");

    static Rental rental0 = new Rental("mitsos", 995566777, "AD4152", 5, 45, "2023-04-30", "true", "true", 0);
    static Rental rental1 = new Rental("nina", 0, "s1", 5, 45, "2023-07-03", "true", "false", 0);

}
