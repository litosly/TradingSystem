package entities;

import java.io.Serializable;
import java.util.UUID;

/**
 * An class that represents an item
 */
public class Item implements Serializable {
    private String itemId;
    private String itemName;
    private String description;
    private String ownerName;
    private String type;

    public String getType() {
        return type;
    }

    /**
     * itemID, itemName, description, ownerName are required to create an instance of item.
     * @param itemName
     * @param description
     * @param ownerName
     * @param type
     */



    public Item (String itemName, String description, String ownerName, String type){
        this.itemId = UUID.randomUUID().toString();
        this.itemName = itemName;
        this.description = description;
        this.ownerName = ownerName;
        this.type = type;

    }



    /**
     * Setting the item's ownerId
     * @param newId
     */
    public void setOwnerName(String newId){
        this.ownerName = newId;
    }

    /**
     * setting the name of the item
     * @param itemName
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    /**
     * setting the description of the item
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * return the item id
     * @return itemId
     */
    public String getItemId(){
        return itemId;
    }

    /**
     * return the owner's name
     * @return ownerName
     */
    public String getOwnerName(){
        return ownerName;
    }

    /**
     * return the item's description
     * @return description
     */
    public String getDescription(){
        return description;
    }

    /**
     * retrun the name of the item
     * @return itemName
     */
    public String getItemName(){
        return itemName;
    }

    @Override
    public String toString() {
        return itemId + ", " + itemName + ", "+ description + ", "+ ownerName + ", " + type;
    }
}
