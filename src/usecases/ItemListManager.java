package usecases;

import entities.*;
import presenter.PromptPresenter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;

import static gateway.FileReadAndWrite.TRADE_SET_UP_PROMPT;

public class ItemListManager {
    private ItemList itemList;
    private ClientUserManager clientUserManager;
    private int editAppointmentLimit = 3;

    // David's code change. changed because I want to move Read and Write away from usecase and into gateway.
    public ItemListManager(ClientUserManager clientUserManager) {
        this.clientUserManager = clientUserManager;
    }

    public ItemList getItemList() {
        return this.itemList;
    }

    // for user to call when adding to
    public void addItemToWishList(Item item) {
        clientUserManager.getCurrentUser().addToWishList(item);
    }

    //for admin to call when approving an item
    public void addItemToInventory(Item item) {
        clientUserManager.getCurrentUser().addToInventory(item);
    }

    private void addItemToPendingList(Item item) {
        clientUserManager.getCurrentUser().addToPendingItem(item);
    }

    public void createAnItem(String itemName, String description, String type) {
        addItemToPendingList(new Item(itemName, description, clientUserManager.getCurrentUser().getUserName(), type));
    }

    // TODO: Implement Strategy design pattern
    public boolean showAllPendingItem() {
        boolean notEmpty = false;
        for (ClientUser clientUser : clientUserManager.getClientUserList().getAllClientUser()) {
            if (!clientUser.getPendingItemList().isEmpty()) {
                System.out.println(clientUser.getPendingItemList().toString());
                notEmpty = true;
            }
        }
        if (notEmpty) {
            return true;
        } else {
            System.out.println("No Pending Item");
            return false;
        }
    }

    public boolean approveItem(String id) {
        for (ClientUser clientUser : clientUserManager.getClientUserList().getActiveUser()) {
            for (Item item : clientUser.getPendingItemList().getItems()) {
                if (item.getItemId().equals(id)) {
                    clientUser.addToInventory(item);
                    clientUser.getPendingItemList().getItems().remove(item);
                    if (clientUser.getInventory().getItems().size() >= 3) {
                        clientUser.setAccountStatus("VIP");
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public boolean removeItem(String id) {
        for (ClientUser clientUser : clientUserManager.getClientUserList().getActiveUser()) {
            for (Item item : clientUser.getPendingItemList().getItems()) {
                if (item.getItemId().equals(id)) {
                    System.out.println("About to remove the pending item");
                    clientUser.getPendingItemList().getItems().remove(item);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean removeAppointment(String id) {
        for (ClientUser clientUser : clientUserManager.getClientUserList().getActiveUser()) {
            for (Appointment appointment : clientUser.getPendingAppointments().getAppointmentList()) {
                if (appointment.getId().equals(id)) {
                    System.out.println("About to remove the pending appointment");
                    clientUser.getPendingAppointments().getAppointmentList().remove(appointment);
                    System.out.println(clientUser.toString());
                    System.out.println(clientUser.getPendingAppointments().toString());
                    return true;
                }
            }
        }
        return false;
    }

    public boolean editAppointment(String id, ClientUser curUser) throws IOException {

        for (ClientUser clientUser : clientUserManager.getClientUserList().getActiveUser()) {
            for (Appointment appointment : clientUser.getPendingAppointments().getAppointmentList()) {
                if (appointment.getId().equals(id)) {
                    // Check if User Edit Count exceed limit
                    boolean limitExceed1 = false;
                    boolean limitExceed2 = false;
                    if (appointment.getUser1EditsCount()>this.editAppointmentLimit) {
                        System.out.println("User 1 Edit Count exceeds limit");
                        limitExceed1 = true;
                        if (appointment.getUsername1().equals(curUser.getUserName())){
                            return false;
                        }
                    }
                    if (appointment.getUser2EditsCount()>this.editAppointmentLimit) {
                        System.out.println("User 2 Edit Count exceeds limit");
                        limitExceed2 = true;
                        if (appointment.getUsername2().equals(curUser.getUserName())){
                            return false;
                        }
                    }
                    if (limitExceed1 && limitExceed2){
                        System.out.println("Both edit limit exceeds, pending appointment canceled");
                        removeAppointment(appointment.getId());
                        System.out.println("Appointment Removed");
                        return true;
                    }

                    System.out.println("About to Edit Pending Appointment");
                    // Edit, read input from curUser
                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                    System.out.println("When you want to meet instead?");
                    String input = br.readLine();
                    System.out.println("Where you want to meet instead?");
                    String input2 = br.readLine();

                    appointment.setTime(input);
                    appointment.setAddress(input2);

                    // Increment User Edit Count
                    appointment.incrementUserEditsCount(curUser.getUserName());
                    return true;
                }
            }
        }
        return false;
    }
    public boolean confirmAppointment(String id, ClientUser curUser) {
        /**
         * Confirm Appointment that are pending,
         * First extract info in the pending appointment
         * Then create new transaction using the info
         * Put the transaction ticket into both clients' inventory
         * Finally delete the transaction
         * */
        for (ClientUser clientUser : clientUserManager.getClientUserList().getActiveUser()) {
            for (Appointment appointment : clientUser.getPendingAppointments().getAppointmentList()) {
                if (appointment.getId().equals(id)) {
                    System.out.println("About to confirm the pending appointment");
                    // Create TransactionTicket
                    TransactionTicket transactionTicket = new TransactionTicket(
                            appointment.getItem1(),
                            appointment.getTime(),
                            appointment.getUsername1(), // previous Proposer now becomes receiver
                            appointment.getUsername2(), // previous Receiver now becomes proposer
                            appointment.getId());

                    // Add to both users transaction ticket and confirmed appointment list
                    ClientUser Proposer = findUserByUsername(appointment.getUsername1());
                    ClientUser Receiver = findUserByUsername(appointment.getUsername2());
                    Proposer.getConfirmedAppointments().addToAppointment(appointment);
                    Receiver.getConfirmedAppointments().addToAppointment(appointment);
                    Proposer.getPendingTransaction().addToTransactionTicketList(transactionTicket);
                    Receiver.getPendingTransaction().addToTransactionTicketList(transactionTicket);

                    // Finally REMOVE appointment in transaction id
                    Proposer.getPendingAppointments().getAppointmentList().remove(appointment);
                    Receiver.getPendingAppointments().getAppointmentList().remove(appointment);
                    return true;
                }
            }
        }
        return false;
    }




    public void showAllUserInventories() {
        for (ClientUser clientUser : clientUserManager.getClientUserList().getActiveUser()) {
            System.out.println(clientUser.toString());
            System.out.println(clientUser.getInventory().toString());
        }
    }

    public Item findItemByItemId(String id) {
        for (ClientUser clientUser : clientUserManager.getClientUserList().getActiveUser()) {
            for (Item item : clientUser.getInventory().getItems()) {
                if (item.getItemId().equals(id)) {
                    return item;
                }
            }
        }
        return null;
    }

    public ClientUser findUserByItemId(String id) {
        for (ClientUser clientUser : clientUserManager.getClientUserList().getActiveUser()) {
            for (Item item : clientUser.getInventory().getItems()) {
                if (item.getItemId().equals(id)) {
                    return clientUser;
                }
            }
        }
        return null;
    }

    public ClientUser findUserByUsername(String username) {
        for (ClientUser clientUser : clientUserManager.getClientUserList().getActiveUser()) {
            if (clientUser.getUserName().equals(username)) {
                return clientUser;
            }
        }
        return null;
    }

    public boolean addItemToWishList(String id) {
        for (ClientUser clientUser : clientUserManager.getClientUserList().getActiveUser()) {
            for (Item item : clientUser.getInventory().getItems()) {
                if (item.getItemId().equals(id)) {
                    ClientUser userToAdd = clientUserManager.getCurrentUser();
                    userToAdd.addToWishList(item);
                    return true;
                }
            }
        }
        return false;
    }


    public ItemList getItemListByUser(String username) {
        ItemList itemList = new ItemList();
        for (Item item : this.itemList.getItems()) {
            if (item.getOwnerName().equalsIgnoreCase(username)) {
                itemList.getItems().add(item);
            }
        }
        return itemList;
    }

    public ArrayList<Item> getMatchedItems(String regEx, String type) {
        ArrayList<Item> filteredList = new ArrayList<>();
        for (Item item : itemList.getItems()) {
            if (item.getItemName().toLowerCase().matches(regEx.toLowerCase()) && item.getType().equals(type)) {
                filteredList.add(item);
            }
        }
        return filteredList;
    }

    public ArrayList<Item> getAvailableItems(String type) {
        ArrayList<Item> filteredList = new ArrayList<>();
        for (Item item : itemList.getItems()) {
            if (type == null) {
                filteredList.add(item);
            } else if (item.getType().equals(type)) {
                filteredList.add(item);
            }
        }
        return filteredList;
    }


    public boolean isContains(String name) {
        AtomicBoolean found = new AtomicBoolean(false);
        itemList.getItems().forEach((item) -> {
            if (item.getItemName().equals(name))
                found.set(true);
        });
        return found.get();
    }

    public void addItem(Item item) {
        itemList.addToItemList(item);
    }

    public boolean confirmTransaction(String id, ClientUser curUser) {
        /**
         * Confirm Transaction that are pending,
         * In: transaction id, curUser who is confirming
         * Out: Boolean result whether confirmation is complete
         *
         * First extract info in the pending Transaction
         * Then Set ticket isUser1Confirmed/isUser2Confirmed to be true
         * Check if both confirmed
         * Append to Transaction history upon both user approved and
         * 1. Remove item from wishlist if it's there
         * 2. Remove item from lending item
         * 4. update system time (TODO)
         * 5. calculate for return time if temporary transaction (TODO)
         * Finally delete the pending transaction
         * */
        TransactionTicket targettransactionTicket = null;
        ClientUser proposer = null;
        ClientUser receiver = null;
        ClientUser otherUser;
        Item item1;
        Item item2;
        Appointment confirmedAppointment;
        for (ClientUser clientUser : clientUserManager.getClientUserList().getActiveUser()) {
            for (TransactionTicket transactionTicket : clientUser.getPendingTransaction().getTransactionTicketList()) {
                if (transactionTicket.getAppointmentId().equals(id)) {
                    targettransactionTicket = transactionTicket;
                    proposer = clientUserManager.getUserByUsername(targettransactionTicket.getProposer());
                    receiver = clientUserManager.getUserByUsername(targettransactionTicket.getReceiver());
                    System.out.println(" -- Found Current Transaction");
                }
            }
        }
        if (targettransactionTicket == null) {
            System.out.println(" -- Cannot Find Specified Pending Transaction");
            return false;
        }
        System.out.println(" -- About to confirm the pending transaction");
        // Confirm cur user and the other user's identity
        if (curUser.getUserName().equals(proposer.getUserName())) {
            otherUser = receiver;
            System.out.println(" -- Current User is proposer");
        } else {
            otherUser = proposer;
            System.out.println(" -- Current User is receiver");
        }
        // Confirm transaction by updating isUser1Confirmed/isUser2Confirmed
        if (targettransactionTicket.confirm(curUser.getUserName(), true)) {
            System.out.println(" -- CurUser Confirmed Transaction Successfully.");
            confirmedAppointment = curUser.getConfirmedAppointments().getAppointmentList().get(0);
            item1 = confirmedAppointment.getItem1();
            item2 = confirmedAppointment.getItem2();
        } else{
            System.out.println(" -- Confirm Transaction Failed");
            return false;
        }

        // Check if both proposer and receiver has confirmed the transaction.
        if (targettransactionTicket.getIsUser1Confirmed()) {
            if (targettransactionTicket.getIsUser2Confirmed()) {
                // ------------- transaction all confirmed -----------
                targettransactionTicket.setIsCompleted(true);
                // Put into history
                System.out.println(" -- Putting Transaction in Transaction History");
                proposer.getHistory().addToTransactionTicketList(targettransactionTicket);
                receiver.getHistory().addToTransactionTicketList(targettransactionTicket);

                // 1. remove item from wishlist
                // check if it's in wishlist
                System.out.println(" -- Removing Item(s) from User WishLists");
                for (ClientUser tempUser: clientUserManager.getClientUserList().getActiveUser()) {
                    List<Item> wishedItems = tempUser.getWishList().getItems();
                    for (Item wishedItem : wishedItems) {
                        if (wishedItem.equals(item1)) {
                            tempUser.getWishList().getItems().remove(wishedItem);
                        }
                        if (wishedItem.equals(item2)){
                            tempUser.getWishList().getItems().remove(wishedItem);
                        }
                    }
                }

                // 2. Remove item from lending item
                System.out.println(" -- Removing Item(s) from User Lending List");
                otherUser.getInventory().getItems().remove(item1);
                if (item2 != null){
                    curUser.getInventory().getItems().remove(item2);
                }

                // 3. Update User Status
                System.out.println(" -- Updating User Status");
                if (curUser.getInventory().getItems().size() < 3){
                    curUser.setAccountStatus("active");
                }
                if (otherUser.getInventory().getItems().size() < 3){
                    otherUser.setAccountStatus("active");
                }

                // Finally delete the pending transaction
                System.out.println(" -- Deleting the pending transactions");
                curUser.getPendingTransaction().getTransactionTicketList().remove(targettransactionTicket);
                otherUser.getPendingTransaction().getTransactionTicketList().remove(targettransactionTicket);
                System.out.println(" -- Deleting the confirmed appointments");
                curUser.getConfirmedAppointments().getAppointmentList().remove(confirmedAppointment);
                otherUser.getConfirmedAppointments().getAppointmentList().remove(confirmedAppointment);
                return true;
            }
        }
        System.out.println("Waiting For the other user to confirm");
        return true;
    }


    public ArrayList<TransactionTicket> getRecentTransactions(int numTransactions, ClientUser currentUser){
        ArrayList<TransactionTicket> transactions = new ArrayList<>();
        List<TransactionTicket> history = currentUser.getHistory().getTransactionTicketList();
        if (numTransactions <= history.size()){
            for (int i = 0; i<numTransactions; i++){
                transactions.add(history.get(history.size()-i-1));
            }
        } else{
            transactions.addAll(history);
        }
        return transactions;
    }

    public void printRecentTransactions(int numTransactions, ClientUser currentUser){
        ArrayList<TransactionTicket> recentTransactions = getRecentTransactions(numTransactions, currentUser);
        for (TransactionTicket transactionTicket: recentTransactions){
            System.out.println(transactionTicket.toString());
        }
    }

    public HashMap<String, Integer> getTradePartners(ClientUser currentUser){
        String curUserName = currentUser.getUserName();
        String partnerName = null;
        HashMap<String, Integer> tradingPartners = new HashMap<>();
        List<TransactionTicket> history = currentUser.getHistory().getTransactionTicketList();
        for (TransactionTicket transactionTicket:history){
            if (transactionTicket.getProposer().equals(curUserName)){
                partnerName = transactionTicket.getReceiver();
            }
            else if (transactionTicket.getReceiver().equals(curUserName)){
                partnerName = transactionTicket.getProposer();
            }
            // Add partner count
            if (partnerName != null) {
                // Increment Count of tradingPartner or insert if not seen before
                int count = tradingPartners.getOrDefault(partnerName, 0);
                tradingPartners.put(partnerName, count + 1);
            }
        }
        return tradingPartners;
    }

    public void printTradingPartners(int numPartners, ClientUser currentUser){
        HashMap<String, Integer> tradingPartners = getTradePartners(currentUser);
        List<Entry<String, Integer>> greatest = findGreatest(tradingPartners, numPartners);
        System.out.println("Top "+ numPartners +" Trading Partners:");
        for (Entry<String, Integer> entry : greatest){
            System.out.println(entry.getKey());
        }
    }

    private static <K, V extends Comparable<? super V>> List<Entry<K, V>>
    findGreatest(Map<K, V> map, int n)
    {
        Comparator<? super Entry<K, V>> comparator =
                (Comparator<Entry<K, V>>) (e0, e1) -> {
                    V v0 = e0.getValue();
                    V v1 = e1.getValue();
                    return v0.compareTo(v1);
                };
        PriorityQueue<Entry<K, V>> highest =
                new PriorityQueue<>(n, comparator);
        for (Entry<K, V> entry : map.entrySet())
        {
            highest.offer(entry);
            while (highest.size() > n)
            {
                highest.poll();
            }
        }

        List<Entry<K, V>> result = new ArrayList<>();
        while (highest.size() > 0)
        {
            result.add(highest.poll());
        }
        return result;
    }
}
