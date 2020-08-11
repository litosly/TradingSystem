package gateway;

import entities.*;
import usecases.AdminUserManager;
import usecases.ClientUserManager;
import usecases.ItemListManager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static gateway.FileReadAndWrite.*;

public class AdminUserReadWrite {
    private static final Logger logger = Logger.getLogger(ClientUserReadWrite.class.getName());
    AdminUserManager am;
    /*public AdminUserManager createAdminUserFromCSV(String adminUserListCSV) {
        List<AdminUser> adminUserList = new ArrayList<>();
        String[] adminUserStringList = adminUserListCSV.split(System.lineSeparator());
        //System.out.println("LENGTH" + adminUserStringList.length);
        for(int i = 1; i< adminUserStringList.length; i++){
            //skip title
            //System.out.println(adminUserStringList[i]);

            AdminUser adminUser = createAdminUser(adminUserStringList[i]);


            adminUserList.add(adminUser);
        }
        return new AdminUserManager(adminUserList);
    }

    private AdminUser createAdminUser(String adminUserString){
        String [] adminUserStringArray = adminUserString.split(",");
        String username = adminUserStringArray[0];
        String password = adminUserStringArray[1];
        boolean isInitAdminUser = Boolean.parseBoolean(adminUserStringArray[2]);
        return new AdminUser(username, password, isInitAdminUser);
    }

    public void addNewAdminToFile(String username, String password) {
        FileReadAndWrite.appendLineToFilestring ID(ADMIN_USER_FILE, username+","+password+","+"false");
    }

 */
    public AdminUserManager createClientUserManagerFromFile(String path) throws ClassNotFoundException {
        readFromFile(path);
        return am;
    }

    private void readFromFile(String path) throws ClassNotFoundException {

        try {
            InputStream file = new FileInputStream(path);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);

            am = (AdminUserManager) input.readObject();
            input.close();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Cannot read from input.", ex);
        }
    }

    public void saveToFile(String path, AdminUserManager adminUserManager) throws IOException {
        OutputStream file = new FileOutputStream(path);
        OutputStream buffer = new BufferedOutputStream(file);
        ObjectOutput output = new ObjectOutputStream(buffer);

        output.writeObject(adminUserManager);
        output.close();
    }


}
