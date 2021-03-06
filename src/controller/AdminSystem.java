package controller;

import entities.*;
import gateway.ClientUserReadWrite;
import gateway.FileReadAndWrite;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.*;

import static gateway.FileReadAndWrite.*;
import gateway.ClientUserReadWrite;

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
                    for (ClientUser user : clientUserManager.getClientUserList().getAllClientUser()){
                        if (user.getAccountStatus().equals("pending")){
                            System.out.println(user);
                        }
                    }
                    run();
                case "2":
                    // show pending items
                    approvePendingItem();
                    run();
                case "3":
                    //review pending transactions
                    for (ClientUser clientUser : clientUserManager.getClientUserList().getActiveUser()) {
                        for (TransactionTicket transactionTicket : clientUser.getPendingTransaction().getTransactionTicketList()) {
                            System.out.println(transactionTicket);
                        }
                    }
                    run();
                case "4":
                    System.out.println("To Edit Threshold Values, Please manually change in database/thresholds.csv.");
                    run();
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
                    // Show Threshold Value
                    System.out.println(clientUserReadWrite.readThresholdsFromCSV(THRESHOLDMANAGER_FILE));
                    run();
                case "10":
                    ArrayList<String> signupadmininput = PromptPresenter.takeInputLineByLine(CREATE_ADMIN_PROMPT);
                    createNewAdminUser(signupadmininput.get(0), signupadmininput.get(1));
                case "11":
                    System.out.println("Remove Pending Appointment");
                    removePendingAppointment();
                    run();
                case "12":
                    System.out.println("Remove Pending Items");
                    removePendingItems();
                    run();
                case "13":
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                    LocalDateTime now = LocalDateTime.now();
                    System.out.println(dtf.format(now));
                case "14":
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

    private void removePendingItems() throws IOException, ClassNotFoundException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        if (itemListManager.showAllPendingItem()) {
            System.out.println("please type in the id of the item to remove");
            String input = br.readLine();
            if (itemListManager.removeItem(input)) {
                System.out.println("Item removed.");
                clientUserReadWrite.saveToFile(CLIENT_USER_FILE,clientUserManager);
                run();
            } else {
                System.out.println("somethign is wrong, please try again.");
                removePendingItems();
            }
        } else {run();}
    }

    private void removePendingAppointment() throws IOException, ClassNotFoundException {
        clientUserManager.printAllUsers();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("please type in the id of the appointment to remove pending appointments");
        String input = br.readLine();
        if (itemListManager.removeAppointment(input)) {
            System.out.println("Appointment removed.");
            clientUserReadWrite.saveToFile(CLIENT_USER_FILE,clientUserManager);
            run();
        } else {
            System.out.println("somethign is wrong, please try again.");
            removePendingItems();
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

