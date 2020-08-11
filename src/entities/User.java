package entities;

import java.io.Serializable;

public abstract class User implements Serializable {

    private String userName;
    private String password;

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;

    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public boolean passwordMatch(String password) {
        return password.equals(this.password);
    }

    // TODO: make sure this has a reason
    public abstract boolean isAdminUser();


}
