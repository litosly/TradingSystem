package controller;

import gateway.AdminUserReadWrite;
import gateway.ClientUserReadWrite;
import gateway.FileReadAndWrite;
import gateway.ThresholdReadWrite;
import presenter.PromptPresenter;
import presenter.PropertiesIterator;
import usecases.AdminUserManager;
import usecases.ClientUserManager;
import usecases.ThresholdManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static gateway.FileReadAndWrite.*;


public class LoginSystem implements InputProcessable{
    StartMenuSystem startMenuSystem = new StartMenuSystem();
    ClientUserReadWrite clientUserReadWrite = new ClientUserReadWrite();
    AdminUserReadWrite adminUserReadWrite = new AdminUserReadWrite();
    ThresholdReadWrite thresholdReadWrite = new ThresholdReadWrite();
    ClientUserManager clientUserManager = clientUserReadWrite.createClientUserManagerFromFile(CLIENT_USER_FILE);
    AdminUserManager adminUserManager = adminUserReadWrite.createClientUserManagerFromFile(ADMIN_USER_FILE);

    public LoginSystem() throws ClassNotFoundException {
    }

    //reference ReadWriteEx project
    public void run() throws IOException, ClassNotFoundException {

        processInput(PromptPresenter.takeInputLineByLine(LOGIN_PROMPT));

    }

    public void processInput(ArrayList<String> inputArray) throws IOException, ClassNotFoundException {
        String username = inputArray.get(0);
        String password = inputArray.get(1);
        if(inputArray.get(2).equals("1")){
                adminLogin(username, password);
        }
        else if(inputArray.get(2).equals("2")){
                clientLogin(username, password);
        }
    }

    private void adminLogin(String username, String password) throws IOException, ClassNotFoundException {
        boolean loginSuccess = adminUserManager.login(username, password);
        if(loginSuccess){
            System.out.println("Admin Login Success!");
            //launch AdminUserTradingSystem
            if(adminUserManager.isCurrentAdminUserInitial()){
                InitialAdminSystem ias= new InitialAdminSystem(adminUserManager, clientUserManager);
                ias.run();
            }else {
                AdminSystem as = new AdminSystem(clientUserManager);
                as.run();
            }
        }else {
            System.out.println("Incorrect Username / Password, Please try again");
            run();
        }
    }

    private void clientLogin(String username, String password) throws IOException, ClassNotFoundException {
        boolean loginSuccess = clientUserManager.login(username, password);
        if(loginSuccess){
            System.out.println("Client Login Success!");
            //launch ClientUserTradingSystem
            ClientUserSystem clientUserSystem = new ClientUserSystem(clientUserManager);
            clientUserSystem.run();
        }else {
            System.out.println("Incorrect Username / Password, Please try again");
            run();
        }
    }

}
