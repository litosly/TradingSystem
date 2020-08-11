package entities;

public class AdminUser extends User {

    // If the user is the initial admin
    private final boolean isInitialAdminUser;

    public AdminUser(String userName, String password, boolean isInitAdminUser) {
        super(userName, password);
        this.isInitialAdminUser = isInitAdminUser;
    }

    public boolean isAdminUser() {
        return true;
    }

    public boolean isInitialAdminUser(){
        return isInitialAdminUser;
    }

    @Override
    public String toString() {
        return "IsInitialAdminUser: " + this.isInitialAdminUser + ", Username: " + this.getUserName() + ", Password: " + this.getPassword();
    }
}
