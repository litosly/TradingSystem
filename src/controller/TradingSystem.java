package controller;

import entities.Appointment;
import entities.ClientUser;
import entities.Item;
import entities.TransactionTicket;
import gateway.ClientUserReadWrite;
import presenter.ItemListPresenter;
import presenter.PromptPresenter;
import usecases.ClientUserManager;
import usecases.ItemListManager;
import usecases.TradeManager;
import usecases.TransactionTicketManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import gateway.ClientUserReadWrite.*;
import static gateway.FileReadAndWrite.*;

public class TradingSystem implements InputProcessable{
    private ClientUserManager clientUserManager;
    private TradeManager tradeManager;
    private ItemListManager itemListManager ;
    private ClientUserReadWrite curw = new ClientUserReadWrite();
    ClientUserReadWrite clientUserReadWrite = new ClientUserReadWrite();
    private ItemListPresenter itemListPresenter;
    private int numTry = 0;
    private int maxNumTry = 2;
    private int incompleteTransactionLimit;

    public TradingSystem(ClientUserManager clientUserManager){
        this.clientUserManager = clientUserManager;
        this.itemListManager = new ItemListManager(clientUserManager);
        this.tradeManager  = new TradeManager(clientUserManager);
        this.itemListPresenter = new ItemListPresenter(clientUserManager);
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
            itemListPresenter.showAllUserInventories();
            addItemToWishlist();
            run();
        }
        else if (inputArray.get(0).equals("2")) {
            itemListPresenter.showAllUserInventories();
            requestToTrade();
            run();
        }
        else if (inputArray.get(0).equals("3")) {
            itemListPresenter.showAllUserInventories();
            requestToBurrow();
        }
        else if (inputArray.get(0).equals("4")) {
            confirmAppointment();
            run();
        }
        else if (inputArray.get(0).equals("5")) {
            confirmTransaction();
            run();
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
        String input = br.readLine();
        if(itemListManager.addItemToWishList(input)) {
            this.numTry = 0;
            System.out.println("Wishlist updated. Current wishlist: ");
            System.out.println(clientUserManager.getCurrentUser().getWishList().toString());
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
        this.incompleteTransactionLimit = clientUserReadWrite.readThresholdsFromCSV(THRESHOLDMANAGER_FILE).get("\uFEFFnumIncompleteTransaction");
        if (checkIncompleteTransaction(clientUserManager.getCurrentUser(), this.incompleteTransactionLimit)) {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            ArrayList<String> inputArray = PromptPresenter.takeInputLineByLine(TRADE_SET_UP_PROMPT);
            System.out.println(clientUserManager.getCurrentUser().getInventory().toString());
            System.out.println("Please type in your item id to complete the process.");
            String input = br.readLine();
            if (itemListManager.findItemByItemId(inputArray.get(0)) != null && itemListManager.findItemByItemId(input) != null) {
                // Check if owner of the item account is active or not
                if (itemListManager.findUserByItemId(inputArray.get(0)).getAccountStatus().equals("pending") ||
                        itemListManager.findUserByItemId(inputArray.get(0)).getAccountStatus().equals("frozen") ||
                        itemListManager.findUserByItemId(inputArray.get(0)).getAccountStatus().equals("temporary_left")
                ){
                    System.out.println("Item's owner are frozen or on vocation, please contact admin to confirm");
                    run();
                }
                // Creating a new trade object for trading
                Appointment appointment = tradeManager.trade(
                        itemListManager.findItemByItemId(input),
                        itemListManager.findItemByItemId(inputArray.get(0)),
                        inputArray.get(1),
                        inputArray.get(2)
                );
                clientUserManager.getCurrentUser().addToPendingAppointment(appointment);
                itemListManager.findUserByItemId(inputArray.get(0)).addToPendingAppointment(appointment);
                ClientUserReadWrite.saveToFile(CLIENT_USER_FILE, clientUserManager);
            } else {
                System.out.println("Id does not match. Please try again.");
            }
        } else{
            System.out.println("Incomplete Transaction Limit Exceeds, account frozen.");
            clientUserManager.getCurrentUser().setAccountStatus("frozen");
        }

    }

    public void requestToBurrow() throws IOException, ClassNotFoundException {
        // Check Eligibility to confirm transaction
        this.incompleteTransactionLimit = clientUserReadWrite.readThresholdsFromCSV(THRESHOLDMANAGER_FILE).get("\uFEFFnumIncompleteTransaction");
        int numItemsLendBeforeBorrow = clientUserReadWrite.readThresholdsFromCSV(THRESHOLDMANAGER_FILE).get("numItemsLendBeforeBorrow");
        if ( getNumLendedItems(clientUserManager.getCurrentUser()) < numItemsLendBeforeBorrow){
            System.out.println("Not Enough Items in Inventory or Lending History, Cannot Borrow");
            run();
        }
        if (checkIncompleteTransaction(clientUserManager.getCurrentUser(), this.incompleteTransactionLimit)) {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            ArrayList<String> inputArray = PromptPresenter.takeInputLineByLine(TRADE_SET_UP_PROMPT);
            if (itemListManager.findItemByItemId(inputArray.get(0)) != null) {
                // Check if owner of the item account is active or not
                if (itemListManager.findUserByItemId(inputArray.get(0)).getAccountStatus().equals("pending") ||
                        itemListManager.findUserByItemId(inputArray.get(0)).getAccountStatus().equals("frozen") ||
                        itemListManager.findUserByItemId(inputArray.get(0)).getAccountStatus().equals("temporary_left")
                ){
                    System.out.println("Item's owner are frozen or on vocation, please contact admin to confirm");
                    run();
                }
                // create Borrow event
                Appointment appointment = tradeManager.borrow(
                        itemListManager.findItemByItemId(inputArray.get(0)),
                        clientUserManager.getCurrentUser(), // notice now we set the owner of the item to be proposer.
                        inputArray.get(1),
                        inputArray.get(2));
                System.out.println("Appointment Setup Successfully, waiting for the other user to confirm.");
                clientUserManager.getCurrentUser().addToPendingAppointment(appointment);
                itemListManager.findUserByItemId(inputArray.get(0)).addToPendingAppointment(appointment);
                ClientUserReadWrite.saveToFile(CLIENT_USER_FILE, clientUserManager);
                System.out.println(clientUserManager.getCurrentUser().getPendingAppointments());
            } else {
                System.out.println("Id does not match. Please try again.");
            }
        } else{
            System.out.println("Incomplete Transaction Limit Exceeds, account frozen.");
            clientUserManager.getCurrentUser().setAccountStatus("frozen");
        }
    }

    public boolean checkIncompleteTransaction(ClientUser curUser, int limit){
        int count = curUser.getPendingTransaction().getTransactionTicketList().size();
        return count < limit;
    }

    public int getNumLendedItems(ClientUser curUser){
        int count = 0;
        for (TransactionTicket transactionTicket: curUser.getHistory().getTransactionTicketList()){
            if (transactionTicket!= null){
                if (transactionTicket.getProposer().equals(curUser.getUserName()) ||
                    transactionTicket.getReceiver().equals(curUser.getUserName())
                ){
                    count ++;
                }
            }
        }
        return count;
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
        System.out.println("please let us know if you would like to edit by entering 'y'");
        String input2 = br.readLine();
        if (input2.equals("y")){
            if (itemListManager.editAppointment(input, clientUserManager.getCurrentUser())){
                System.out.println("Appointment Edited.");
            } else{
                System.out.println("Edit Appointment Failed, Please Try again.");
            }
        } else {
            if (itemListManager.confirmAppointment(input,clientUserManager.getCurrentUser())) {
                System.out.println("Appointment confirmed.");
                ClientUserReadWrite.saveToFile(CLIENT_USER_FILE, clientUserManager);
            } else {
                System.out.println("Confirm Appointment Failed, please try again.");
            }
        }
    }

    public void confirmTransaction() throws IOException, ClassNotFoundException {
        // Print Pending Transactions
        List<TransactionTicket> ticketList = clientUserManager.getCurrentUser().getPendingTransaction().getTransactionTicketList();
        for (TransactionTicket ticket : ticketList) {
            System.out.println(ticket.toString());
        }

        if (clientUserManager.getCurrentUser().getPendingTransaction().getTransactionTicketList().isEmpty()) {
            System.out.println("No Current Pending Transaction");
            run();
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("please type in the id of the transaction to confirm");
        String input = br.readLine();
        if (itemListManager.confirmTransaction(input, clientUserManager.getCurrentUser())) {
            System.out.println("Transaction confirmed by user.");
            ClientUserReadWrite.saveToFile(CLIENT_USER_FILE, clientUserManager);
            run();
        } else {
            System.out.println("something is wrong, please try again.");
            confirmAppointment();
        }
    }
}
