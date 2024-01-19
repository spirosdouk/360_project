package database_init;

import java.math.BigDecimal;
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

    static User user0 = new User("mitsos", "letsee!", "Mitsos Mitsakis", "Kalokairinou 112, Heraklion", "2002-01-21", 995566777, new BigDecimal(9));
    static User user1 = new User("nina", "aaa111!", "Katerina Nina", "Kalokairinou 112, Heraklion", "2007-01-21", 0, new BigDecimal(8));
    static User user2 = new User("mpampis", "aaa111!", "Mpampis", "Kalokairinou 112, Heraklion", "1997-02-15", 112233445, new BigDecimal(7));
    static User user3 = new User("tsitsip", "aaa111!", "Tsitsipas", "Kalokairinou 112, Heraklion", "2000-01-21", 153248697, new BigDecimal(5));
    static User user4 = new User("sakari", "aaa111!", "Maria Sakari", "Kalokairinou 112, Heraklion", "2001-05-21", 147852369, new BigDecimal(4));
    static User user5 = new User("dasdsadsai", "dasdasdasd", "Madasdasdasi", "Kdasdasdasdasdasderaklion", "2001-05-21", 0, new BigDecimal(4));


    static Vehicle veh0 = new Vehicle("Fiat", "Panda", "blue", "car", "AD4152", 320, 30, 15, "false", "city car", "true");
    static Vehicle veh4 = new Vehicle("Kia", "Sportage", "gray", "car", "TH5263", 320, 30, 15, "true", "SUV", "false");
    static Vehicle veh5 = new Vehicle("Kia", "Sportage", "red", "car", "FK5161", 320, 30, 15, "false", "SUV", "false");
    static Vehicle veh6 = new Vehicle("Fiat", "Panda", "gray", "car", "FK5123", 320, 30, 15, "false", "city car", "false");
    static Vehicle veh7 = new Vehicle("Jaguar", "F-Type", "gray", "car", "LY1515", 320, 40, 20, "false", "Super Car", "false");


    static Vehicle veh1 = new Vehicle("Rad Power", "RadExpand", "black", "bicycle", "b1", 70, 15, 8, "false", "", "false");
    static Vehicle veh8 = new Vehicle("Swagtron", "EB7 Elite", "red", "bicycle", "b2", 70, 15, 8, "false", "", "false");
    static Vehicle veh9 = new Vehicle("NAKTO", "Santa Monica", "white", "bicycle", "b3", 70, 15, 8, "false", "", "false");
    static Vehicle veh10 = new Vehicle("NAKTO", "Discovery", "black", "bicycle", "b4", 70, 15, 8, "false", "", "false");

    static Vehicle veh2 = new Vehicle("Yamaha", "Bolt", "gray", "bike", "JD4522", 180, 25, 12, "false", "", "false");
    static Vehicle veh11 = new Vehicle("BMW", "K1600 B", "black", "bike", "JE5632", 180, 25, 12, "false", "", "false");
    static Vehicle veh12 = new Vehicle("DUCATI", "MULTISTRADA", "red", "bike", "GP4852", 180, 25, 12, "false", "", "false");
    static Vehicle veh13 = new Vehicle("HARLEY", "ULTRA", "black", "bike", "HD5981", 180, 25, 12, "false", "", "false");

    static Vehicle veh3 = new Vehicle("Glion", "Balto", "silver", "scooter", "s1", 320, 30, 15, "false", "", "true");
    static Vehicle veh14 = new Vehicle("Glion", "Balto", "black", "scooter", "s3", 320, 30, 15, "false", "", "false");
    static Vehicle veh15 = new Vehicle("Glion", "Balto", "gray", "scooter", "s2", 320, 30, 15, "false", "", "false");
    static Vehicle veh16 = new Vehicle("Glion", "Balto", "red", "scooter", "s4", 320, 30, 15, "false", "", "false");

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
    static Maintenance maint4 = new Maintenance("FK5161", 150, "2024-01-18", "2024-01-21", "repair", "ongoing");

    static Rental rental0 = new Rental("mitsos", 995566777, "AD4152", 5, 45, "2023-04-30", "true", "true", "none");
    static Rental rental1 = new Rental("nina", 0, "s1", 5, 45, "2023-07-03", "true", "false", "none");
    static Rental rental2 = new Rental("sakari", 147852369, "GP4852", 5, 37, "2023-05-30", "true", "true", "HD5981");
    static Rental rental3 = new Rental("tsitsip", 0, "b2", 5, 15, "2023-10-03", "true", "false", "none");
    static Rental rental4 = new Rental("tsitsip", 995566777, "AD4152", 5, 45, "2023-12-13", "true", "true", "none");
    static Rental rental5 = new Rental("sakari", 229933777, "GP4852", 5, 37, "2024-01-03", "true", "true", "none");

}
