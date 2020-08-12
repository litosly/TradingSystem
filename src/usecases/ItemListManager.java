package usecases;

import entities.*;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class ItemListManager {
    private ItemList itemList;
    private ClientUserManager clientUserManager;

    // David's code change. changed because I want to move Read and Write away from usecase and into gateway.
    public ItemListManager(ClientUserManager clientUserManager){
        this.clientUserManager = clientUserManager;
    }

    public ItemList getItemList(){
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
        for (ClientUser clientUser: clientUserManager.getClientUserList().getAllClientUser()) {
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
        for (ClientUser clientUser: clientUserManager.getClientUserList().getActiveUser()) {
            for (Item item: clientUser.getPendingItemList().getItems()) {
                if (item.getItemId().equals(id)) {
                    clientUser.addToInventory(item);
                    clientUser.getPendingItemList().getItems().remove(item);
                    if (clientUser.getInventory().getItems().size() >= 3){
                        clientUser.setAccountStatus("VIP");
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public boolean removeItem(String id) {
        for (ClientUser clientUser: clientUserManager.getClientUserList().getActiveUser()) {
            for (Item item: clientUser.getPendingItemList().getItems()) {
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
        for (ClientUser clientUser: clientUserManager.getClientUserList().getActiveUser()) {
            for (Appointment appointment: clientUser.getPendingAppointments().getAppointmentList()) {
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
        for (ClientUser clientUser: clientUserManager.getClientUserList().getActiveUser()) {
            for (Appointment appointment: clientUser.getPendingAppointments().getAppointmentList()) {
                if (appointment.getId().equals(id)) {
                    System.out.println("About to confirm the pending appointment");

                    TransactionTicket transactionTicket = new TransactionTicket(
                            appointment.getItem1(),
                            appointment.getTime(),
                            appointment.getUsername1(), // Proposer
                            appointment.getUsername2(), // Receiver
                            appointment.getId());

                    // Add to both users transaction ticket
                    ClientUser clientUser1 = findUserByUsername(appointment.getUsername1());
                    ClientUser clientUser2 = findUserByUsername(appointment.getUsername2());
                    clientUser1.getPendingTransaction().addToTransactionTicketList(transactionTicket);
                    clientUser2.getPendingTransaction().addToTransactionTicketList(transactionTicket);
//                    System.out.println(clientUser1.getPendingTransaction().toString());
//                    System.out.println(clientUser2.getPendingTransaction().toString());

                    // Finally REMOVE appointment in transaction id
                    clientUser1.getPendingAppointments().getAppointmentList().remove(appointment);
                    clientUser2.getPendingAppointments().getAppointmentList().remove(appointment);
                    return true;
                }
            }
        }
        return false;
    }


    public void showAllUserInventories() {
        for (ClientUser clientUser: clientUserManager.getClientUserList().getActiveUser()) {
            System.out.println(clientUser.toString());
            System.out.println(clientUser.getInventory().toString());
        }
    }

    public Item findItemByItemId(String id) {
        for (ClientUser clientUser: clientUserManager.getClientUserList().getActiveUser()) {
            for (Item item : clientUser.getInventory().getItems()) {
                if (item.getItemId().equals(id)) {
                    return item;
                }
            }
        }
        return null;
    }

    public ClientUser findUserByItemId(String id) {
        for (ClientUser clientUser: clientUserManager.getClientUserList().getActiveUser()) {
            for (Item item : clientUser.getInventory().getItems()) {
                if (item.getItemId().equals(id)) {
                    return clientUser;
                }
            }
        }
        return null;
    }

    public ClientUser findUserByUsername(String username) {
        for (ClientUser clientUser: clientUserManager.getClientUserList().getActiveUser()) {
            if (clientUser.getUserName().equals(username)) {
                return clientUser;
            }
        }
        return null;
    }

    public boolean addItemToWishList(String id) {
        for (ClientUser clientUser: clientUserManager.getClientUserList().getActiveUser()) {
            for (Item item: clientUser.getInventory().getItems()) {
                if (item.getItemId().equals(id)) {
                    ClientUser userToAdd = clientUserManager.getCurrentUser();
                    userToAdd.addToWishList(item);
                    System.out.println("Successfully added to wish List for testing");
                    return true;
                }
            }
        }
        return false;
    }



    public ItemList getItemListByUser(String username){
        ItemList itemList = new ItemList();
        for(Item item: this.itemList.getItems()){
            if(item.getOwnerName().equalsIgnoreCase(username)){
                itemList.getItems().add(item);
            }
        }
        return itemList;
    }

    public ArrayList<Item> getMatchedItems(String regEx, String type) {
        ArrayList<Item> filteredList = new ArrayList<>();
        for (Item item: itemList.getItems()) {
            if (item.getItemName().toLowerCase().matches(regEx.toLowerCase()) && item.getType().equals(type)) {
                filteredList.add(item);
            }
        }
        return filteredList;
    }

    public ArrayList<Item> getAvailableItems(String type) {
        ArrayList<Item> filteredList = new ArrayList<>();
        for(Item item: itemList.getItems()) {
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
}
