package controller;

import entities.*;
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
import java.util.List;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

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
        System.out.println("Welcome to the Client System! At any time, 'exit' to quit the system.");
        processInput(PromptPresenter.takeInput(CLIENT_USER_SYSTEM_PROMPT));
    }

    @Override
    public void processInput(ArrayList<String> inputArray) throws ClassNotFoundException, IOException {
        // 1. Look for things to trade
        if (inputArray.get(0).equals("1")) {
            trading();
        }
        // 2. Browse Pending Transactions
        else if (inputArray.get(0).equals("2")) {
            printPendingTransactions();
        }
        // 3. View Recently Traded Items
        else if (inputArray.get(0).equals("3")) {
//            System.out.println(clientUserManager.getCurrentUser().getHistory().getTransactionTicketList().toString());
            itemListManager.printRecentTransactions(3, clientUserManager.getCurrentUser());
        }
        // 4. View Most Frequent Trade Partners
        else if (inputArray.get(0).equals("4")) {
            itemListManager.printTradingPartners(3, clientUserManager.getCurrentUser());
        }
        // 5. Browse Pending Appointments
        else if (inputArray.get(0).equals("5")) {
            System.out.println(clientUserManager.getCurrentUser().getPendingAppointments().toString());
        }
        // 6. Browse Inventory
        else if (inputArray.get(0).equals("6")) {
            itemListManager.showAllUserInventories();
        }
        // 7. View your wish list
        else if (inputArray.get(0).equals("7")) {
            System.out.println(clientUserManager.getCurrentUser().getWishList().toString());
            System.out.println("USER: " + clientUserManager.getCurrentUser());
        }
        // 8. view lending list
        else if (inputArray.get(0).equals("8")) {
            System.out.println(clientUserManager.getCurrentUser().getInventory().toString());
        }
        // 9. Request to add an item
        else if (inputArray.get(0).equals("9")) {
            if (clientUserManager.getCurrentUser().getAccountStatus().equals("frozen")){
                System.out.println("Current Account is frozen, please ask admin to unfreeze it before proceeding to trade");
                run();
            }
            ArrayList<String> input = PromptPresenter.takeInputLineByLine(REQUEST_TO_ADD_ITEM_PROMPT);
            itemListManager.createAnItem(input.get(0),input.get(1),input.get(2));
            System.out.println("Please wait while an admin approves of your added item!");
            run();
        }
        // 10. Request admin to unfreeze account
        else if (inputArray.get(0).equals("10")) {
            if (!clientUserManager.getCurrentUser().getAccountStatus().equals("frozen")){
                System.out.println("Current Account is not frozen.");
                run();
            }
            ClientUserList clientUserList = new ClientUserList();
            clientUserList.removeFromFrozenUser(clientUserManager.getCurrentUser());
            clientUserList.addToPendingUser(clientUserManager.getCurrentUser());
            clientUserManager.getCurrentUser().setAccountStatus("pending");
            System.out.println("Client user status successfully changed to pending");
        }
        // 11. View your Threshold limits
        else if (inputArray.get(0).equals("11")) {
            System.out.println(ThresholdManager.getAllUserThresholds(clientUserManager.getCurrentUser().getUserName()));
        }
        // 12. Recommended Items to Lend
        else if (inputArray.get(0).equals("12")) {
            itemListManager.showAllUserInventories(); // browse inventory
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Please Select Items you like");
            String input = br.readLine();
            if (itemListManager.findUserByItemId(input) != null){
                ClientUser userWithItem = itemListManager.findUserByItemId(input);
                recommendItemsforLending(userWithItem);
            } else{
                System.out.println("Invalid Item Id, please try again");
                run();
            }
        }
        // 13. Get user History
        else if (inputArray.get(0).equals("13")) {
            for (TransactionTicket transactionTicket: clientUserManager.getCurrentUser().getHistory().getTransactionTicketList()){
                System.out.println(transactionTicket.toString());
            }
        }
        // 14. Temporarily Take down
        else if (inputArray.get(0).equals("14")) {
            System.out.println("Setting Current Status to temporary_left");
            clientUserManager.getCurrentUser().setAccountStatus("temporary_left");
        }
        // 15. Back from vocation.
        else if (inputArray.get(0).equals("15")) {

            if (clientUserManager.getCurrentUser().getAccountStatus().equals("temporary_left")){
                System.out.println("Setting Current Status back to active");
                clientUserManager.getCurrentUser().setAccountStatus("active");
            } else{
                System.out.println("Current Status not temporary_left, cannot change to active.");
            }
            run();
        }
        // 15. Get current date time
        else if (inputArray.get(0).equals("16")) {
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

    private void trading() throws IOException, ClassNotFoundException {
        if (clientUserManager.getCurrentUser().getAccountStatus().equals("frozen") ||
                clientUserManager.getCurrentUser().getAccountStatus().equals("temporary_left") ||
                clientUserManager.getCurrentUser().getAccountStatus().equals("pending")){
            System.out.println("Current Account is frozen or on vocation, please ask admin to unfreeze it or set your" +
                    "status back to active before proceeding to trade");
            run();
        }
        TradingSystem tradingSystem = new TradingSystem(clientUserManager);
        tradingSystem.run();
    }

    private void recommendItemsforLending(ClientUser userWithItem) {
        if (!userWithItem.getWishList().isEmpty()) {
            System.out.println("You can try lending him/her the following items since they are in his/her wish list:");
            System.out.println(userWithItem.getWishList());
        } else {
            System.out.println("Try lend anything to the User! (He/She does not have anything in their mind.");
        }
    }

    // pending transactions
    public void printPendingTransactions() {
        List<TransactionTicket> ticketList = clientUserManager.getCurrentUser().getPendingTransaction().getTransactionTicketList();
        for (TransactionTicket ticket : ticketList){
            System.out.println(ticket.toString());
        }
    }
}
