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


public class VisitorSystem implements InputProcessable{
    StartMenuSystem startMenuSystem = new StartMenuSystem();
    ClientUserReadWrite clientUserReadWrite = new ClientUserReadWrite();
    AdminUserReadWrite adminUserReadWrite = new AdminUserReadWrite();
    ThresholdReadWrite thresholdReadWrite = new ThresholdReadWrite();
    ClientUserManager clientUserManager = clientUserReadWrite.createClientUserManagerFromFile(CLIENT_USER_FILE);
    AdminUserManager adminUserManager = adminUserReadWrite.createClientUserManagerFromFile(ADMIN_USER_FILE);
    private String username = "visitor";
    private String password = "123";

    public VisitorSystem() throws ClassNotFoundException {
    }
    //reference ReadWriteEx project
    public void run() throws IOException, ClassNotFoundException {
        processInput(new ArrayList<>());
    }

    public void processInput(ArrayList<String> inputArray) throws IOException, ClassNotFoundException {
        clientLogin(username, password);
    }

    private void clientLogin(String username, String password) throws IOException, ClassNotFoundException {
        boolean loginSuccess = clientUserManager.login(username, password);
        if(loginSuccess){
            System.out.println("Visitor Login Success!");
            //launch VisitorBrowsing System
            ClientUserSystem clientUserSystem = new ClientUserSystem(clientUserManager);
            clientUserSystem.run();
        }else {
            System.out.println("No Visitor Account Available, please contact admin");
            run();
        }
    }

}
