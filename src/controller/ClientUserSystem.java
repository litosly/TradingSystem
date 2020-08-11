package controller;

import entities.ClientUser;
import entities.Item;
import entities.ItemList;
import gateway.AppointmentListReadWrite;
import gateway.ClientUserReadWrite;
import presenter.PromptPresenter;
import usecases.AppointmentManager;
import usecases.ClientUserManager;
import usecases.ItemListManager;
import usecases.ThresholdManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static gateway.FileReadAndWrite.*; // Import all final vals/paths

public class ClientUserSystem implements InputProcessable{
    /**
     * Client User System for actions user can choose from  bafter log in
     */

    private ClientUserManager clientUserManager;
    private ItemListManager itemListManager;
    private ClientUserReadWrite curw = new ClientUserReadWrite();

    public ClientUserSystem(ClientUserManager clientUserManager) throws ClassNotFoundException {
        this.clientUserManager = clientUserManager;
        this.itemListManager = new ItemListManager(clientUserManager);
    }

    public void run() throws ClassNotFoundException, IOException {
//        inventoryListManager = new ItemListManager(FileReadAndWrite.readFromFile(INVENTORY_ITEMS_FILE));
//        clientUserManager.getCurrentUser().setInventory(inventoryListManager.getItemListByUser(clientUserManager.getCurrentUser().getUserName()));
//        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Welcome to the Client System! At any time, 'exit' to quit the system.");
        processInput(PromptPresenter.takeInput(CLIENT_USER_SYSTEM_PROMPT));
    }

    @Override
    public void processInput(ArrayList<String> inputArray) throws ClassNotFoundException, IOException {
        if (inputArray.get(0).equals("1")) {
            trading();
        }
        else if (inputArray.get(0).equals("2")) {
        }
        else if (inputArray.get(0).equals("3")) {
        }
        else if (inputArray.get(0).equals("4")) {
        } //Browse Pending Appointments
        else if (inputArray.get(0).equals("5")) {
            System.out.println(clientUserManager.getCurrentUser().getPendingAppointments().toString());
        }
        else if (inputArray.get(0).equals("6")) {
        }
        else if (inputArray.get(0).equals("7")) {
            System.out.println(clientUserManager.getCurrentUser().getWishList().toString());
            System.out.println("USER: " + clientUserManager.getCurrentUser());
        } // view lending list
        else if (inputArray.get(0).equals("8")) {
            System.out.println(clientUserManager.getCurrentUser().getInventory().toString());
        }

        else if (inputArray.get(0).equals("9")) {
            ArrayList<String> input = PromptPresenter.takeInputLineByLine(REQUEST_TO_ADD_ITEM_PROMPT);
            itemListManager.createAnItem(input.get(0),input.get(1),input.get(2));
            System.out.println("Please wait while an admin approves of your added item!");
            run();
        }

        else if (inputArray.get(0).equals("10")) {
        }

        else if (inputArray.get(0).equals("11")) {
            System.out.println(ThresholdManager.getAllUserThresholds(clientUserManager.getCurrentUser().getUserName()));
        }

        else if (inputArray.get(0).equals("12")) {
            curw.saveToFile(CLIENT_USER_FILE,clientUserManager);
            StartMenuSystem startMenuSystem = new StartMenuSystem();
            startMenuSystem.run();
        }
    }

    private void trading() throws IOException, ClassNotFoundException {
        TradingSystem tradingSystem = new TradingSystem(clientUserManager);
        tradingSystem.run();
    }
}
