package controller;

import entities.Appointment;
import gateway.ClientUserReadWrite;
import presenter.PromptPresenter;
import usecases.ClientUserManager;
import usecases.ItemListManager;
import usecases.TradeManager;
import usecases.TransactionTicketManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import gateway.ClientUserReadWrite;

import static gateway.FileReadAndWrite.*;

public class TradingSystem implements InputProcessable{
    private ClientUserManager clientUserManager;
    private TradeManager tradeManager;
    private ItemListManager itemListManager ;
    private ClientUserReadWrite curw = new ClientUserReadWrite();
    ClientUserReadWrite clientUserReadWrite = new ClientUserReadWrite();
    private int numTry = 0;
    private int maxNumTry = 2;

    public TradingSystem(ClientUserManager clientUserManager){
        this.clientUserManager = clientUserManager;
        this.itemListManager = new ItemListManager(clientUserManager);
        this.tradeManager  = new TradeManager(clientUserManager);
    }



    public void run() throws IOException, ClassNotFoundException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Welcome to the Trading System! At any time, 'exit' to quit the system.");
        processInput(PromptPresenter.takeInput(TRADE_SYSTEM_PROMPT));
    }

    @Override
    public void processInput(ArrayList<String> inputArray) throws IOException, ClassNotFoundException {
        /**
         * 1. Add an item to wishlist
         * 2. Request to Trade
         * 3. Request to Borrow
         * 4. Confirm an Appointment
         * 4. Return to Client menu
         * */
        if (inputArray.get(0).equals("1")) {
            itemListManager.showAllUserInventories();
            addItemToWishlist();
        }
        else if (inputArray.get(0).equals("2")) {
            itemListManager.showAllUserInventories();
            requestToTrade();
        }
        else if (inputArray.get(0).equals("3")) {
            itemListManager.showAllUserInventories();
            requestToBurrow();
        }
        else if (inputArray.get(0).equals("4")) {
            confirmAppointment();
        }
        // Going back
        else {
            ClientUserReadWrite.saveToFile(CLIENT_USER_FILE,clientUserManager);
            ClientUserSystem clientUserSystem = new ClientUserSystem(clientUserManager);
            clientUserSystem.run();
        }
    }

    private void addItemToWishlist() throws IOException, ClassNotFoundException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("please type in the id of the item to approve");
        String input = br.readLine();
        if(itemListManager.addItemToWishList(input)) {
            this.numTry = 0;
            System.out.println("Wishlist updated.");
            ClientUserReadWrite.saveToFile(CLIENT_USER_FILE,clientUserManager);
            run();
        }
        else if (this.numTry < this.maxNumTry) {
            System.out.println("No ID matches your input, try again.");
            this.numTry ++;
            addItemToWishlist();
        } else{
            ClientUserReadWrite.saveToFile(CLIENT_USER_FILE,clientUserManager);
            ClientUserSystem clientUserSystem = new ClientUserSystem(clientUserManager);
            clientUserSystem.run();
        }

    }

    private void requestToTrade() throws IOException, ClassNotFoundException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<String> inputArray = PromptPresenter.takeInputLineByLine(TRADE_SET_UP_PROMPT);
        System.out.println(clientUserManager.getCurrentUser().getInventory().toString());
        System.out.println("Please type in your item id to complete the process.");
        String input = br.readLine();
        if (itemListManager.findItemByItemId(inputArray.get(0)) != null && itemListManager.findItemByItemId(input) != null) {
            // Creating a new trade object for trading
            Appointment appointment = tradeManager.trade(
                    itemListManager.findItemByItemId(input),
                    itemListManager.findItemByItemId(inputArray.get(0)),
                    inputArray.get(1),
                    inputArray.get(2)
            );
            clientUserManager.getCurrentUser().addToPendingAppointment(appointment);
            itemListManager.findUserByItemId(inputArray.get(0)).addToPendingAppointment(appointment);
            ClientUserReadWrite.saveToFile(CLIENT_USER_FILE,clientUserManager);
            run();
        } else {
            System.out.println("Id does not match. Please try again.");
            requestToTrade();
        }

    }

    public void requestToBurrow() throws IOException, ClassNotFoundException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<String> inputArray = PromptPresenter.takeInputLineByLine(TRADE_SET_UP_PROMPT);
        if (itemListManager.findItemByItemId(inputArray.get(0)) != null){ //&& itemListManager.findItemByItemId(input) != null) {
            Appointment appointment = tradeManager.borrow(
                    itemListManager.findItemByItemId(inputArray.get(0)),
                    clientUserManager.getCurrentUser(),
                    inputArray.get(1),
                    inputArray.get(2));
            System.out.println("Appointment Setup Successfully, waiting for the other user to confirm.");
            clientUserManager.getCurrentUser().addToPendingAppointment(appointment);
            itemListManager.findUserByItemId(inputArray.get(0)).addToPendingAppointment(appointment);
            ClientUserReadWrite.saveToFile(CLIENT_USER_FILE,clientUserManager);
            System.out.println(clientUserManager.getCurrentUser().getPendingAppointments());
            run();
        } else {
            System.out.println("Id does not match. Please try again.");
            requestToBurrow();
        }
    }

    public void confirmAppointment() throws IOException, ClassNotFoundException {
        System.out.println(clientUserManager.getCurrentUser().getPendingAppointments().toString());
        if (clientUserManager.getCurrentUser().getPendingAppointments().getAppointmentList().isEmpty()){
            System.out.println("No Current Pending Appointment");
            run();
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("please type in the id of the appointment to confirm");
        String input = br.readLine();
        if (itemListManager.confirmAppointment(input)) {
            System.out.println("Appointment confirmed.");
            ClientUserReadWrite.saveToFile(CLIENT_USER_FILE,clientUserManager);
                run();
            } else {
                System.out.println("somethign is wrong, please try again.");
                confirmAppointment();
            }
    }

}
