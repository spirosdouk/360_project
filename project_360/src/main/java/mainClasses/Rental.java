package mainClasses;

/**
 *
 * @author dimos
 */
public class Rental {

    int rental_id, driv_lic, duration, total_cost, daily_cost, car_change;
    String rental_date, is_returned, has_insurance, username, lic_plate;

    // Setter methods
    public void setRental_id(int rental_id) {
        this.rental_id = rental_id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setDriv_lic(int driv_lic) {
        this.driv_lic = driv_lic;
    }

    public void setLic_plate(String lic_plate) {
        this.lic_plate = lic_plate;
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

    public void setHas_insurance(String has_insurance) {
        this.has_insurance = has_insurance;
    }

    public void setDaily_cost(int daily_cost) {
        this.daily_cost = daily_cost;
    }

    public void setIs_returned(String is_returned) {
        this.is_returned = is_returned;
    }

    public void setCar_change(int car_change) {
        this.car_change = car_change;
    }

    // Getter methods
    public int getDaily_cost() {
        return daily_cost;
    }

    public int getRental_id() {
        return rental_id;
    }

    public String getUsername() {
        return username;
    }

    public int getDriv_lic() {
        return driv_lic;
    }

    public String getLic_plate() {
        return lic_plate;
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

    public String getHas_insurance() {
        return has_insurance;
    }

    public int getCar_change() {
        return car_change;
    }

    public Rental(String username, int _driv_lic, String _lic_plate, int _duration, int _daily_cost, String _rental_date, String _is_returned, String _has_insurance, int _car_change) {
        this.username = username;
        this.driv_lic = _driv_lic;
        this.lic_plate = _lic_plate;
        this.duration = _duration;
        this.daily_cost = _daily_cost;
        this.total_cost = _duration * _daily_cost;
        this.rental_date = _rental_date;
        this.is_returned = _is_returned;
        this.has_insurance = _has_insurance;
        this.car_change = _car_change;
    }
}
