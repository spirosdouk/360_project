package mainClasses;

/**
 *
 * @author dimos
 */
public class Maintenance {

    int maintenance_id, vehicle_id, cost;
    String start_date, end_date, maint_type, status;

    // Setter methods
    public void setMaintenance_id(int maintenance_id) {
        this.maintenance_id = maintenance_id;
    }

    public void setVehicle_id(int vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public void setMaint_type(String maint_type) {
        this.maint_type = maint_type;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Getter methods
    public int getMaintenance_id() {
        return maintenance_id;
    }

    public int getVehicle_id() {
        return vehicle_id;
    }

    public int getCost() {
        return cost;
    }

    public String getStart_date() {
        return start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public String getMaint_type() {
        return maint_type;
    }

    public String isStatus() {
        return status;
    }
}
