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
    private String accountStatus = "active"; //we have active, pending (to be frozen/unfrozen by admin), and frozen;
    private int numBurrowed = 0;             //the fourth status is VIP
    private int numLent = 0;
    private int incompleteLimit = 3;
    private int transactionLimit = 7;
//    private


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
        return this.inventory;
    }

    public int getTransactionLimit() {
        return transactionLimit;
    }

    public int getIncompleteLimit() { return incompleteLimit; }

    public int getNumBurrowed() { return numBurrowed; }

    public int getNumLent() { return numLent; }

    public AppointmentList getConfirmedAppointments() {
        return this.confirmedAppointments;
    }

    public ItemList getPendingItemList() {
        return pendingItemList;
    }

    public TransactionTicketList getHistory() {
        return history;
    }

    public TransactionTicketList getPendingTransaction() {
        return this.pendingTransaction;
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

    public void addToPendingAppointment(Appointment appointment) {this.pendingAppointments.addToAppointment(appointment);}

    public void addToConfirmedAppointment(Appointment appointment) {this.confirmedAppointments.addToAppointment(appointment);}

    @Override // We can add more to this if we want to distinguish more. (GOES USERNAME, PASSWORD, NONOBJECT VARIABLE IN THE ORDER OF ABOVE)
    public String toString() {
//        return "Username: " + this.getUserName() + ", Password: " + this.getPassword() + "," + accountStatus + "," + numBurrowed + "," +
//                numLent + "," + incompleteLimit + "," + transactionLimit;
        return "Username: " + this.getUserName() + ", account status: " + accountStatus + ", num borrowed: " + numBurrowed
                + ", number lent: " + numLent + "Secret source: " + this.getPassword(); // + ", " + incompleteLimit + "," + transactionLimit;
    }

}
