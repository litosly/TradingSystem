package usecases;

import entities.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ItemListManager {
    private ItemList itemList;
    private ClientUserManager clientUserManager;

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

    public boolean confirmAppointment(String id) {
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
                            appointment.getUsername1(), // Proposer
                            appointment.getUsername2(), // Receiver
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
}
