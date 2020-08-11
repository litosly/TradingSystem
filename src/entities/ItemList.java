package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemList implements Serializable {
    private List<Item> items;

    // TODO: what is the reason of this id


    public ItemList(){
        this.items = new ArrayList<>();
    }

    public void addToItemList(Item item){
        items.add(item);
    }

    public void addToItemListNoDuplicates(Item item) {
        if (items.isEmpty()) {
            items.add(item);
        }
        for (Item itemSelected: items) {
            if (item.getItemId().equals(itemSelected.getItemId())) {
                System.out.println("Added item already in the list!");
            }
            else {
                items.add(item);
            }

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
