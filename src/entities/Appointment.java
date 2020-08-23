package entities;
import java.io.Serializable;
import java.util.UUID;

/**
 * A class that represents an appointment
 */
public class Appointment implements Serializable {
    private String appointmentId;
    // Time is not constraint to numerical value so that we can have "anytime" query from user
    private String time; //"year-month-day-time" ex."2020-07-09-13:00"
    private String address;
    private String proposer; //user1 username
    private String receiver; //user2 username
    private boolean isConfirmed; //boolean value of whether the appointment is confirmed
    private boolean isUpdated;
    private int user1EditsCount;
    private int user2EditsCount;
    private Item item1;
    private Item item2;


    /**
     * user1Id, user2Id, time, address, appointmentId are required to create an instance of Entities.Appointment
     * @param user1Id
     * @param user2Id
     * @param time
     * @param address
     */

    //appointment for 2 items
    public Appointment(Item item1, Item item2, String user1Id, String user2Id, String time, String address){
        this.proposer = user1Id;
        this.receiver = user2Id;
        this.time = time;
        this.address = address;
        this.appointmentId = UUID.randomUUID().toString();
        this.user1EditsCount = 0;
        this.user2EditsCount = 0;
        this.isConfirmed = false;
        this.isUpdated = false;
        this.item1 = item1;
        this.item2 = item2;
    }

    //appointment for 1 item
    public Appointment(Item item1, String user1Id, String user2Id, String time, String address){
        this.proposer = user1Id;
        this.receiver = user2Id;
        this.time = time;
        this.address = address;
        this.appointmentId = UUID.randomUUID().toString();
        this.user1EditsCount = 0;
        this.user2EditsCount = 0;
        this.isConfirmed = false;
        this.isUpdated = false;
        this.item1 = item1;
    }

    // David's code, I need to set each variable when reading from CVS
    public Appointment(String appointmentId, String time, String address, String userOneName, String receiver, boolean isConfirmed, boolean isUpdated, int user1EditsCount, int user2EditsCount) {
        this.appointmentId = appointmentId;
        this.time = time;
        this.address = address;
        this.proposer = userOneName;
        this.receiver = receiver;
        this.isConfirmed = isConfirmed; //boolean value of whether the appointment is confirmed
        this.isUpdated = isUpdated;
        this.user1EditsCount = user1EditsCount;
        this.user2EditsCount = user2EditsCount;
    }

    /**
     * setting the time of the appointment
     * @param newTime
     */
    public void setTime(String newTime){
        time = newTime;
    }

    /**
     * setting the address of the location for the appointment to take place
     * @param newAddress
     */
    public void setAddress(String newAddress) {
        address = newAddress;
    }

    /**
     * setting the update status of the appointment
     * @param newBool
     */
    public void setIsUpdated(boolean newBool){ isUpdated = newBool; }

    /**
     * return the time of the appointment
     * @return time
     */
    public String getTime(){
        return time;
    }

    /**
     * return the address of the location for the appointment to take place
     * @return
     */
    public String getAddress(){
        return address;
    }

    /**
     * return the confirmation status of the appointment
     * @return isConfirmed
     */
    public boolean getIsConfirmed() {
        return isConfirmed;
    }

    /**
     * the update status of the appointment
     * @return isUpdated
     */
    public boolean getIsUpdated() {
        return isUpdated;
    }

    /**
     * returns whether the appointment is confirmed.
     */
    public void confirm(){
        isConfirmed = true;
    }

    public String getId() { return appointmentId;}

    public String getUsername1() {
        return proposer;
    }

    public String getUsername2() {
        return receiver;
    }

    public void incrementUserEditsCount(String username){
        if (username.equals(proposer)){
            user1EditsCount+=1;
        }
        else if (username.equals(receiver)){
            user2EditsCount+=1;
        }

    }

    public Item getItem1(){
        return item1;
    }
    public Item getItem2(){
        return item2;
    }
    public void setItem1(Item newItem){
        this.item1 = newItem;
    }
    public void setItem2(Item newItem){
        this.item2 = newItem;
    }
    public int getUser1EditsCount() {
        return user1EditsCount;
    }

    public int getUser2EditsCount() {
        return user2EditsCount;
    }


    @Override
    public String toString() {
        return appointmentId + "," + time  + "," + address  + "," + proposer  + "," + receiver + "," + isConfirmed  + "," +
                "," + isUpdated + "," + user1EditsCount + "," + user2EditsCount;
    }

}
