package gateway;

import entities.AppointmentList;
import entities.ClientUserList;
import entities.ItemList;
import entities.TransactionTicketList;
import usecases.AppointmentManager;
import usecases.ClientUserManager;
import usecases.ItemListManager;
import usecases.TransactionTicketManager;

import java.io.*;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static gateway.FileReadAndWrite.*;

public class ClientUserReadWrite {
    private static final Logger logger = Logger.getLogger(ClientUserReadWrite.class.getName());
    private static final Handler consoleHandler = new ConsoleHandler();
    ClientUserManager cm;

    /*
   ItemListReadWrite itemListReadWrite = new ItemListReadWrite();
   AppointmentListReadWrite appointmentListReadWrite = new AppointmentListReadWrite();
   TransactionTicketReadWrite transactionTicketReadWrite = new TransactionTicketReadWrite();

    public ClientUserManager createClientUserManagerFromCSV(String clientUserCsv){
        ClientUserManager clientUserManager = new ClientUserManager();
        ItemListManager wishListManager = itemListReadWrite.createItemListFromCSV(WISHLIST_ITEM_FILE);
        ItemListManager inventoryManager = itemListReadWrite.createItemListFromCSV(INVENTORY_ITEMS_FILE);
        ItemListManager pendingItemManager = itemListReadWrite.createItemListFromCSV(PENDING_ITEMS_FILE);
        TransactionTicketManager historyManager = transactionTicketReadWrite.createTransactionTicketManagerFromCSV();
        TransactionTicketManager pendingTransManager = transactionTicketReadWrite.createTransactionTicketManagerFromCSV();
        AppointmentManager appointmentmanager = appointmentListReadWrite.createAppointmentListFromCSV(APPOINTMENTS_FILE);

        String[] clientUserStringList = clientUserCsv.split(System.lineSeparator());
        for(int i = 1; i< clientUserStringList.length; i++){
            //skip title
            String[] clientUserStringArray = clientUserStringList[i].split(",");
            ItemList wishList = wishListManager.getItemListByUser(clientUserStringArray[0]);
            ItemList inventory = inventoryManager.getItemListByUser(clientUserStringArray[0]);
            ItemList pendingItem = pendingItemManager.getItemListByUser(clientUserStringArray[0]);
            TransactionTicketList history = historyManager.getHistoryTransactionTicketByUser(clientUserStringArray[0]);
            TransactionTicketList pendingTrans = historyManager.getPendingTransactionTicketByUser(clientUserStringArray[0]);
            AppointmentList appointmentList = appointmentmanager.getAppointmentByUser(clientUserStringArray[0]);
            clientUserManager.createClientUser(clientUserStringList[i],inventory,wishList,pendingItem,history,pendingTrans,appointmentList);
        }
        return clientUserManager;
    }

    // TODO: use threshold values instead of hardcoded numbers.
    public void addNewUserToFile(String username, String password) {
        FileReadAndWrite.appendLineToFile(CLIENT_USER_FILE, username+","+password+",active,3,7,0,0");
    }

     */


    public ClientUserManager createClientUserManagerFromFile(String path) throws ClassNotFoundException {
        readFromFile(path);
        return cm;
    }

    private void readFromFile(String path) throws ClassNotFoundException {

        try {
            InputStream file = new FileInputStream(path);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);

            cm = (ClientUserManager) input.readObject();
            input.close();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Cannot read from input.", ex);
        }
    }

    public static void saveToFile(String path, ClientUserManager clientUserManager) throws IOException {
        OutputStream file = new FileOutputStream(path);
        OutputStream buffer = new BufferedOutputStream(file);
        ObjectOutput output = new ObjectOutputStream(buffer);

        output.writeObject(clientUserManager);
        output.close();
    }
}
