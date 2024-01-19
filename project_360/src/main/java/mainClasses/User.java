package mainClasses;

import java.math.BigDecimal;

/**
 *
 * @author dimos
 */
public class User {
    int user_id;
    String password, name, address, birthdate, username;
    long driv_lic;
    BigDecimal credit_card;

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setCredit_card(BigDecimal credit_card) {
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

    public String getUsername() {
        return username;
    }

    public int getUser_id() {
        return user_id;
    }

    public BigDecimal getCredit_card() {
        return credit_card;
    }

    public long getDriv_lic() {
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
    public User() {
    }

    public User(String _username, String _password, String _name, String _address, String _birthdate, long _driv_lic, BigDecimal _credit_card) {
        this.username = _username;
        this.password = _password;
        this.name = _name;
        this.address = _address;
        this.driv_lic = _driv_lic;
        this.birthdate = _birthdate;
        this.credit_card = _credit_card;
    }
}
