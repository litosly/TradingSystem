package controller;

import entities.ClientUserList;
import gateway.ClientUserReadWrite;
import presenter.ItemListPresenter;
import presenter.PromptPresenter;
import usecases.ClientUserManager;
import usecases.ItemListManager;
import usecases.ThresholdManager;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static gateway.FileReadAndWrite.*; // Import all final vals/paths

public class VisitorBrowsingSystem implements InputProcessable{
    /**
     * Client User System for actions user can choose from  bafter log in
     */

    private ClientUserManager clientUserManager;
    private ItemListManager itemListManager;
    private ClientUserReadWrite curw = new ClientUserReadWrite();
    private ItemListPresenter itemListPresenter;

    public VisitorBrowsingSystem(ClientUserManager clientUserManager) throws ClassNotFoundException {
        this.clientUserManager = clientUserManager;
        this.itemListManager = new ItemListManager(clientUserManager);
        this.itemListPresenter = new ItemListPresenter(clientUserManager);
    }

    public void run() throws ClassNotFoundException, IOException {
        System.out.println("Welcome to the Visitor System! At any time, 'exit' to quit the system.");
        processInput(PromptPresenter.takeInput(VISITOR_PROMPT));
    }

    @Override
    public void processInput(ArrayList<String> inputArray) throws ClassNotFoundException, IOException {
        // 1. Browse Inventory
        if (inputArray.get(0).equals("1")) {
            itemListPresenter.showAllUserInventories();
        }
        else if (inputArray.get(0).equals("2")){
            System.out.println("Showing all registered Users");
            clientUserManager.printAllUsers();
        }
        // 11. View Threshold Values
        else if (inputArray.get(0).equals("3")) {
            System.out.println(ThresholdManager.getAllUserThresholds(clientUserManager.getCurrentUser().getUserName()));
        }
        else if (inputArray.get(0).equals("4")) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            LocalDateTime now = LocalDateTime.now();
            System.out.println(dtf.format(now));
        }
        // log out
        else {
            curw.saveToFile(CLIENT_USER_FILE,clientUserManager);
            StartMenuSystem startMenuSystem = new StartMenuSystem();
            startMenuSystem.run();
        }
        run();
    }
}