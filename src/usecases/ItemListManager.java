package usecases;

import entities.ClientUser;
import entities.Item;
import entities.ItemList;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class ItemListManager {
    private ItemList itemList;
    private ClientUserManager clientUserManager;

    // OLD CODE FROM EDEN
    /*
        public ItemListManager(String itemListCSVFile){
        itemList = createItemListFromCSV(itemListCSVFile);
    }

    public ItemList getItemList(){
        return this.itemList;
    }

    // TODO: move this into a gateway
    private ItemList createItemListFromCSV(String itemListCSVFile) {
        List<Item> itemList = new ArrayList<>();
        String[] items = itemListCSVFile.split(System.lineSeparator());
        for (int i = 1; i < items.length; i++) {
            //skip title
            Item item = createItem(items[i]);
            itemList.add(item);
        }
        return new ItemList(itemList);
    }
     */

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

    public boolean addItemToWishList(String id) {
        for (ClientUser clientUser: clientUserManager.getClientUserList().getActiveUser()) {
            for (Item item: clientUser.getInventory().getItems()) {
                if (item.getItemId().equals(id)) {
                    ClientUser userToAdd = clientUserManager.getCurrentUser();
                    //System.out.println(userToAdd.toString());
                    userToAdd.addToWishList(item);
                    //System.out.println("ADDED:" + userToAdd.getWishList().toString());
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
