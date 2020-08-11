package entities;

import java.util.ArrayList;


public class ClientUser extends User {

    private ItemList wishList;
    private ItemList inventory;
    private ItemList pendingItemList; //to be approved by admin
    private TransactionTicketList history;
    private TransactionTicketList pendingTransaction;
    private AppointmentList confirmedAppointments;
    private AppointmentList pendingAppointments;
    private String accountStatus = "active"; //we have active, pending (to be frozen by admin), and frozen;
    private int numBurrowed = 0;             //the fourth status could be invisible (phase 2).
    private int numLent = 0;
    private int incompleteLimit = 3;
    private int transactionLimit = 7;

    /*public ClientUser(String userName,
                      String password,
                      ItemList wishList,
                      ItemList inventory,
                      ItemList pendingItemList,
                      TransactionTicketList history,
                      TransactionTicketList pendingTransaction,
                      AppointmentList appointmentList,
                      String accountStatus,
                      int numBurrowed,
                      int numLent,
                      int incompleteLimit,
                      int transactionLimit) {
        super(userName, password);
        this.wishList = wishList;
        this.inventory = inventory;
        this.pendingItemList = pendingItemList;
        this.history = history;
        this.pendingTransaction = pendingTransaction;
        this.appointmentList = appointmentList;
        this.accountStatus = accountStatus;
        this.numBurrowed = numBurrowed;
        this.numLent = numLent;
        this.incompleteLimit = incompleteLimit;
        this.transactionLimit = transactionLimit;
    }

     */

    public ClientUser(String userName, String password) {
        super(userName, password);
        wishList = new ItemList();
        inventory = new ItemList();
        pendingItemList = new ItemList();
        history = new TransactionTicketList(new ArrayList<>());
        pendingTransaction = new TransactionTicketList(new ArrayList<>());
        confirmedAppointments = new AppointmentList(new ArrayList<>());
        pendingAppointments = new AppointmentList(new ArrayList<>());
    }

    public void setInventory(ItemList inventory){
        this.inventory = inventory;
    }

    public ItemList getWishList() {
        return wishList;
    }

    public ItemList getInventory() {
        return inventory;
    }

    public int getTransactionLimit() {
        return transactionLimit;
    }

    public int getIncompleteLimit() { return incompleteLimit; }

    public int getNumBurrowed() { return numBurrowed; }

    public int getNumLent() { return numLent; }

    public AppointmentList getConfirmedAppointments() {
        return confirmedAppointments;
    }

    public ItemList getPendingItemList() {
        return pendingItemList;
    }

    public TransactionTicketList getHistory() {
        return history;
    }

    public TransactionTicketList getPendingTransaction() {
        return pendingTransaction;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public AppointmentList getPendingAppointments() {
        return pendingAppointments;
    }

    public void setIncompleteLimit(int num) {
        this.incompleteLimit = num;
    }

    public void setTransactionLimit(int num) {
        this.transactionLimit = num;
    }

    public void setAccountStatus(String status) { accountStatus = status; }

    public boolean isAdminUser() {
        return false;
    }

    public void addToWishList(Item item) {
        wishList.addToItemListNoDuplicates(item);
    }

    public void addToInventory(Item item) {
        inventory.addToItemList(item);
    }

    public void addToPendingItem(Item item) {pendingItemList.addToItemList(item);}

    public void addToPendingAppointment(Appointment appointment) {pendingAppointments.addToAppointment(appointment);}

    public void addToConfirmedAppointment(Appointment appointment) {confirmedAppointments.addToAppointment(appointment);}

    @Override // We can add more to this if we want to distinguish more. (GOES USERNAME, PASSWORD, NONOBJECT VARIABLE IN THE ORDER OF ABOVE)
    public String toString() {
        return "Username: " + this.getUserName() + ", Password: " + this.getPassword() + "," + accountStatus + "," + numBurrowed + "," +
                numLent + "," + incompleteLimit + "," + transactionLimit;
    }

}
