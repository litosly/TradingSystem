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

        StartMenuSystem startMenuSystem = new StartMenuSystem();
        startMenuSystem.run();
    }
}
