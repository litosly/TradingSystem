import controller.StartMenuSystem;
import entities.ClientUser;
import gateway.AdminUserReadWrite;
import gateway.ClientUserReadWrite;
import gateway.ThresholdReadWrite;
import usecases.AdminUserManager;
import usecases.ClientUserManager;
import usecases.ThresholdManager;

import java.io.IOException;
import static gateway.FileReadAndWrite.*;


public class TradingMain {

    public static void main(String[] args) throws ClassNotFoundException, IOException {

        /*AdminUserManager am = new AdminUserManager();
        am.createInitAdmin("admin","password");
        AdminUserReadWrite au = new AdminUserReadWrite();
        au.saveToFile(ADMIN_USER_FILE,am);


        ThresholdManager tm = new ThresholdManager();
        ThresholdReadWrite tr = new ThresholdReadWrite();
        tr.saveToFile(THRESHOLDMANAGER_FILE, tm);


        ClientUserManager cm = new ClientUserManager();
        cm.createUserAccount("user","password");
        ClientUserReadWrite cu = new ClientUserReadWrite();
        cu.saveToFile(CLIENT_USER_FILE,cm);

         */
        StartMenuSystem startMenuSystem = new StartMenuSystem();
        startMenuSystem.run();
    }
}
