package usecases;

import entities.AdminUser;
import entities.AdminUserList;
import entities.ClientUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AdminUserManager implements Serializable {

    // TODO: why do we need current admin user and a list? what if more than 2 are logged in?
    private AdminUserList adminUserList;
    private AdminUser currentAdminUser;

    // OLD EDEN CODE
    /*public AdminUserManager(String adminUserListCSV){
        adminUserList = createAdminUserFromCSV(adminUserListCSV);
    } */

    public AdminUserManager(AdminUserList adminUserList){
        this.adminUserList = adminUserList;
    }

    public AdminUserManager() {this.adminUserList = new AdminUserList();}

    public AdminUserList getAdminUserList() {
        return adminUserList;
    }

    public boolean login(String userName, String password){
        for(AdminUser adminUser : adminUserList.getAllAdminUser()){
            if(adminUser.getUserName().equals(userName)){
                if(adminUser.passwordMatch(password)){
                    currentAdminUser = adminUser;
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    //This method is for testing purposes, will delete later
    public void createInitAdmin(String username, String password) {
        AdminUser ad = new AdminUser(username,password,true);
        adminUserList.addToAllAdminUser(ad);
        adminUserList.addToInitialAdminUser(ad);
    }

    public boolean signUpAdminUser(String username, String password){
        for(AdminUser adminUser: adminUserList.getAllAdminUser()){
            if(adminUser.getUserName().equalsIgnoreCase(username)){
                return false;
            }
        }
        AdminUser newAdminUser = new AdminUser(username, password, false);
        // TODO DAVID CHANGE THIS TO FUNCTION IN ADMINUSERREADANDWRITE
        adminUserList.addToAllAdminUser(newAdminUser);
        adminUserList.addToNonInitialAdminUser(newAdminUser);
        return true;
    }

    public boolean isCurrentAdminUserInitial(){
        return currentAdminUser.isInitialAdminUser();
    }

    public void printAllAdminUsers() {
            for (AdminUser admin: adminUserList.getAllAdminUser()) {
                System.out.println(admin.toString());
            }
    }


}
