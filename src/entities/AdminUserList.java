package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AdminUserList implements Serializable {
    private List<AdminUser> allAdminUser;
    private List<AdminUser> initialAdmin;
    private List<AdminUser> nonInitialAdmin;

    public AdminUserList() {
        allAdminUser = new ArrayList<>();
        initialAdmin = new ArrayList<>();
        nonInitialAdmin = new ArrayList<>();
    }

    public List<AdminUser> getAllAdminUser() {
        return allAdminUser;
    }

    public List<AdminUser> getNonInitialAdmin() {
        return nonInitialAdmin;
    }

    public void addToAllAdminUser(AdminUser adminUser) {allAdminUser.add(adminUser);}

    public void addToInitialAdminUser(AdminUser adminUser) {initialAdmin.add(adminUser);}

    public void addToNonInitialAdminUser(AdminUser adminUser) {initialAdmin.add(adminUser);}

    private List<AdminUser> getInitialAdmins()
            // Functor method for possible extendability
    {
        List<AdminUser> adminList = new ArrayList<AdminUser>();
        for (AdminUser a : allAdminUser)
        {
            if (a.isInitialAdminUser())
            {
                adminList.add(a);
            }
        }
        return adminList;
    }

}
