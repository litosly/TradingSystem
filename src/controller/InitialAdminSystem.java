package controller;

import gateway.AdminUserReadWrite;
import gateway.FileReadAndWrite;
import presenter.PromptPresenter;
import usecases.AdminUserManager;
import usecases.ClientUserManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static gateway.FileReadAndWrite.ADMIN_USER_FILE;
import static gateway.FileReadAndWrite.*;

public class InitialAdminSystem extends AdminSystem implements InputProcessable{
    AdminUserManager adminUserManager;
    AdminUserReadWrite adminUserReadWrite= new AdminUserReadWrite();
    public InitialAdminSystem(AdminUserManager adminUserManager, ClientUserManager clientUserManager) {
        super(clientUserManager);
        this.adminUserManager = adminUserManager;
    }

    // TODO: CURRENTLY DOESN'T USE ADMIN SYSTEM RUN BUT I WILL MAKE A MODULE WHERE YOU CAN ADD NEW PROMPTS AND RESUSE ADMIN SYSTEM's
    @Override
    public void run() throws IOException, ClassNotFoundException {
        //super.run();
        System.out.println("Welcome to the Admin System! At any time, 'exit' to quit the system.");
        processInput(PromptPresenter.takeInput(INITIAL_ADMIN_USER_SYSTEM_PROMPT));
    }

    @Override
    public void processInput(ArrayList<String> inputArray) throws IOException, ClassNotFoundException {
        if (inputArray.get(0).equals("12")) {
            ArrayList<String> input = PromptPresenter.takeInputLineByLine(CREATE_ADMIN_PROMPT);
            createNewAdminUser(input.get(0), input.get(1));
            run();
        }
        else {
            super.processInput(inputArray);
        }
    }

    public void createNewAdminUser(String username, String password) throws IOException {
        if(adminUserManager.signUpAdminUser(username, password)){
            System.out.println("Admin User Account Created: " + username);
            adminUserReadWrite.saveToFile(ADMIN_USER_FILE, adminUserManager);
        } else{
            System.out.println("Cannot Create Admin User Account, please try again.");
            ArrayList<String> input = PromptPresenter.takeInputLineByLine(CREATE_ADMIN_PROMPT);
            createNewAdminUser(input.get(0), input.get(1));
        }
    }


}
