package controller;

import presenter.PromptPresenter;
import presenter.PropertiesIterator;
import usecases.ClientUserManager;
import gateway.ClientUserReadWrite;
import usecases.ThresholdManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static gateway.FileReadAndWrite.*;

public class SignUpSystem implements InputProcessable{
    ClientUserReadWrite clientUserReadWrite = new ClientUserReadWrite();
    ClientUserManager clientUserManager = clientUserReadWrite.createClientUserManagerFromFile(CLIENT_USER_FILE);

    public SignUpSystem() throws ClassNotFoundException {
    }


    public void run() throws IOException, ClassNotFoundException {
        System.out.println("SignupSystem run start");
        processInput(PromptPresenter.takeInputLineByLine(SIGNUP_PROMPT));
    }


    public void processInput(ArrayList<String> inputArray) throws IOException, ClassNotFoundException {
        String username = inputArray.get(0);
        String password = inputArray.get(1);
        clientSignUp(username, password);

    }

    // TODO: make this update everyones lists and output with default values.
    public void clientSignUp(String username, String password) throws IOException, ClassNotFoundException {
            boolean signUpSuccess = clientUserManager.createUserAccount(username, password);
            if (signUpSuccess) {
                System.out.println("Sign up Success!");
                ThresholdManager tm = new ThresholdManager();
                tm.addUserThreshold(username);
                clientUserReadWrite.saveToFile(CLIENT_USER_FILE, clientUserManager);
                ClientUserSystem clientUserSystem = new ClientUserSystem(clientUserManager);
                clientUserSystem.run();
            } else {
                System.out.println("Username is already taken, please choose another one");
                run();
            }
    }
}
