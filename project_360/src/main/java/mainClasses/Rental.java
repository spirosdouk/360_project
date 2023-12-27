package mainClasses;

/**
 *
 * @author dimos
 */
public class Rental {

    int rental_id, user_id, driv_lic, vehicle_id, duration, total_cost;
    String rental_date, is_returned;

    // Setter methods
    public void setRental_id(int rental_id) {
        this.rental_id = rental_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setDriv_lic(int driv_lic) {
        this.driv_lic = driv_lic;
    }

    public void setVehicle_id(int vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setTotal_cost(int total_cost) {
        this.total_cost = total_cost;
    }

    public void setRental_date(String rental_date) {
        this.rental_date = rental_date;
    }

    public void setIs_returned(String is_returned) {
        this.is_returned = is_returned;
    }

    // Getter methods
    public int getRental_id() {
        return rental_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getDriv_lic() {
        return driv_lic;
    }

    public int getVehicle_id() {
        return vehicle_id;
    }

    public int getDuration() {
        return duration;
    }

    public int getTotal_cost() {
        return total_cost;
    }

    public String getRental_date() {
        return rental_date;
    }

    public String Is_returned() {
        return is_returned;
    }
}
