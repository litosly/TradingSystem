package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class ItemList implements Serializable {
    private List<Item> items;

    public ItemList(){
        this.items = new ArrayList<>();
    }

    public void addToItemList(Item item){
        items.add(item);
    }

    public void addToItemListNoDuplicates(Item item) {
        boolean duplicateFlg = false;

        // Simply add for empty list
        if (items.isEmpty()) {
            items.add(item);
            return;
        }
        // Check Duplicate
        for (Item itemSelected: items) {
            if (item.getItemId().equals(itemSelected.getItemId())) {
                duplicateFlg = true;
                break;
            }
        }
        // Add in item only if no duplicate found
        if (!duplicateFlg){
            items.add(item);
        } else{
            System.out.println("Added item already in the list!");
        }
    }

    public List<Item> getItems(){
        return items;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public String toString() {
        StringBuilder itemsString = new StringBuilder();
        if (!items.isEmpty()) {
            for (Item item : items) {
                itemsString.append(item.toString());
                itemsString.append("\n");
            }
            return itemsString.toString();
        }
        return "This list is empty.";
    }

}
