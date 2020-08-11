package controller;

import entities.*;
import gateway.ClientUserReadWrite;
import presenter.PromptPresenter;
import usecases.AdminUserManager;
import usecases.ClientUserManager;
import usecases.ThresholdManager;
import usecases.TransactionTicketManager;
import usecases.ItemListManager;
import gateway.AdminUserReadWrite;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.*;

import static gateway.FileReadAndWrite.*;

// TODO: put this into the UML
public class AdminSystem implements InputProcessable{
    ClientUserManager clientUserManager;
    TransactionTicketManager transactionTicketManager;
    ItemListManager itemListManager ;
    ClientUserReadWrite clientUserReadWrite = new ClientUserReadWrite();
    AdminUserReadWrite adminUserReadWrite = new AdminUserReadWrite();
    ThresholdManager thresholdManager = new ThresholdManager();
    AdminUserManager adminUserManager = adminUserReadWrite.createClientUserManagerFromFile(ADMIN_USER_FILE);

    public AdminSystem(ClientUserManager clientUserManager) throws ClassNotFoundException {
        this.clientUserManager = clientUserManager;
        this.itemListManager = new ItemListManager(clientUserManager);
    }

    public void run() throws IOException, ClassNotFoundException {
        System.out.println("Welcome to the Admin System! At any time, 'exit' to quit the system.");
        processInput(PromptPresenter.takeInput(ADMIN_USER_SYSTEM_PROMPT));

    }

    public void processInput(ArrayList<String> inputArray) throws IOException, ClassNotFoundException {
        try {
            switch (inputArray.get(0)) {
                case "1":
                    //show pending users
                    clientUserManager.showPendingUsers();
                    break;
                case "2":
                    // show pending items
                    approvePendingItem();
                    break;
                case "3":
                    //review pending transactions
                    System.out.println("Displaying all client users");
                    clientUserManager.printAllUsers();
                    ArrayList<String> input = PromptPresenter.takeInputLineByLine(GET_PENDING_TRANSACTION_PROMPT);
                    transactionTicketManager.getPendingTransactionTicketByUser(input.get(0));
                    break;
                case "4":  // Thresholds

                    ArrayList<String> thresholdMenuInput = PromptPresenter.takeInput(THRESHOLD_PROMPT);
                    if (thresholdMenuInput.get(0).equals("1")) {
                        //1. Add Specific Threshhold to Specific User (String user)
                        ArrayList<String> input1 = PromptPresenter.takeInputLineByLine(ADD_USER_THRESHOLD_PROMPT);
                        thresholdManager.addUserThreshold(input1.get(0));
                    } else if (thresholdMenuInput.get(0).equals("2")) {
                        //2. Change Specific User Threshold (String user, String thresholdName, Double newValue)
                        ArrayList<String> input2 = PromptPresenter.takeInputLineByLine(CHANGE_USER_THRESHOLD_PROMPT);
                        double newValue = Double.parseDouble(input2.get(2));
                        thresholdManager.changeUserThreshold(input2.get(0), input2.get(1), newValue);
                    } else if (thresholdMenuInput.get(0).equals("3")) {
                        //3. Add Universal Threshold(String thresholdName, Double value)
                        ArrayList<String> input3 = PromptPresenter.takeInputLineByLine(CHANGE_THRESHOLD_PROMPT);
                        double newValue = Double.parseDouble(input3.get(1));
                        thresholdManager.addThreshold(input3.get(0), newValue);

                    } else if (thresholdMenuInput.get(0).equals("4")) {
                        //4. Remove Universal Threshold(String thresholdNam)
                        ArrayList<String> input4 = PromptPresenter.takeInputLineByLine(REMOVE_THRESHOLD_PROMPT);
                        thresholdManager.removeThreshold(input4.get(0));

                    } else if (thresholdMenuInput.get(0).equals("5")) {
                        //5. Change Universal Threshold(String thresholdName, Double newValue)
                        ArrayList<String> input5 = PromptPresenter.takeInputLineByLine(CHANGE_THRESHOLD_PROMPT);
                        double newValue = Double.parseDouble(input5.get(1));
                        thresholdManager.changeGlobalThresholdValue(input5.get(0), newValue);
                    }
                    else {
                        run();
                    }
                case "5":
                    clientUserManager.printAllUsers();
                    run();
                case "6":
                    //6. Unfreeze User
                    clientUserManager.printAllUsers();
                    ClientUserList clientUserList = new ClientUserList();
                    List<ClientUser> frozen_list = clientUserList.getFrozenUser();
                    for (ClientUser user: frozen_list){
                        System.out.println(user.getUserName());
                    }
                    ArrayList<String> input6 = PromptPresenter.takeInputLineByLine(UNFREEZE_PROMPT);
                    ClientUser toUnfreeze = clientUserManager.getUserByUsername(input6.get(0));
                    clientUserList.removeFromFrozenUser(toUnfreeze);
                    clientUserList.addToActiveUser(toUnfreeze);
                    System.out.println("Client user status successfully changed to unfrozen");
                    run();
                case "7":
                    //7. Freeze User
                    clientUserManager.printAllUsers();
                    ClientUserList clientUserList2 = new ClientUserList();
                    List<ClientUser> pending_list2 = clientUserList2.getPendingUser();
                    // Prints out a list of pending users.
                    for (ClientUser user: pending_list2) {
                        System.out.println(user.getUserName());
                    }
                    ArrayList<String> input7 = PromptPresenter.takeInputLineByLine(FREEZE_PROMPT);
                    ClientUser toFreeze = clientUserManager.getUserByUsername(input7.get(0));
                    clientUserList2.removeFromPendingUser(toFreeze);
                    clientUserList2.addToFrozenUser(toFreeze);
                    System.out.println("Client user status successfully changed to Frozen");
                case "8":
                    //show admin users
                    System.out.println("Showing All admin users");
                    adminUserManager.printAllAdminUsers();
                    run();
                case "9":
//                    thresholdManager.getAllUserThresholds();
                    run();
                case "10":
                    ArrayList<String> signupadmininput = PromptPresenter.takeInputLineByLine(CREATE_ADMIN_PROMPT);
                    createNewAdminUser(signupadmininput.get(0), signupadmininput.get(1));
                case "11":
                    clientUserReadWrite.saveToFile(CLIENT_USER_FILE,clientUserManager);
                    StartMenuSystem startMenuSystem = new StartMenuSystem();
                    startMenuSystem.run();
                    break;
            }
            run();
        } catch (NullPointerException n) {
            System.out.println("It is empty");
            run();
        }
    }

    private void approvePendingItem() throws IOException, ClassNotFoundException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        if (itemListManager.showAllPendingItem()) {
            System.out.println("please type in the id of the item to approve");
            String input = br.readLine();
            if (itemListManager.approveItem(input)) {
                System.out.println("Item approved.");
                clientUserReadWrite.saveToFile(CLIENT_USER_FILE,clientUserManager);
                run();
            } else {
                System.out.println("somethign is wrong, please try again.");
                approvePendingItem();
            }
        } else {run();}
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

