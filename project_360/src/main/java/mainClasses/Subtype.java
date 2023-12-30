package mainClasses;

/**
 *
 * @author dimos
 */
public class Subtype {
    int capacity;
    String subtype_name;

    public void setSubtype_name(String subtype_name) {
        this.subtype_name = subtype_name;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getSubtype_name() {
        return subtype_name;
    }

    public Subtype(String _name, int _cap) {
        this.capacity = _cap;
        this.subtype_name = _name;
    }
}
