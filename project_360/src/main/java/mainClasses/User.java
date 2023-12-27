package mainClasses;

/**
 *
 * @author dimos
 */
public class User {
    int user_id, credit_card, driv_lic;
    String password, name, address, birthdate, username;

    // Setter methods
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setCredit_card(int credit_card) {
        this.credit_card = credit_card;
    }

    public void setDriv_lic(int driving_licence) {
        this.driv_lic = driving_licence;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Getter methods
    public String getUsername() {
        return username;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getCredit_card() {
        return credit_card;
    }

    public int getDriv_lic() {
        return driv_lic;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getBirthdate() {
        return birthdate;
    }
}
