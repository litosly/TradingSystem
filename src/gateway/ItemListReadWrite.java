package gateway;

import entities.Item;

public class ItemListReadWrite {


    private Item createItem(String itemString) {
        String[] itemProperties = itemString.split(",");
        String id = itemProperties[0];
        String itemName = itemProperties[1];
        String description = itemProperties[2];
        String ownerName = itemProperties[3];
        String type = itemProperties[4];

        return new Item(itemName,description,ownerName,type);
    }

    public void addItemToFile(Item item, String fileName) {
        FileReadAndWrite.appendLineToFile(fileName, item.getItemId() + "," + item.getItemName()  + "," + item.getDescription()  + "," + item.getOwnerName()
                + "," + item.getType());
    }



}
