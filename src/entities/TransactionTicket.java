package entities;

import java.io.Serializable;

public class TransactionTicket implements Serializable {

    private String time;
    private String appointmentId;
    private Item item1;
    private Item item2;
    private String proposer;
    private String receiver;
    private boolean isUser1Confirmed = false; //physically present at scene
    private boolean isUser2Confirmed = false;
    private boolean isCompleted = false;

    // one item(one way)
    public TransactionTicket(Item item1, Item item2, String time, String user1, String user2, String apptId) {
        this.time = time;
        this.proposer = user1;
        this.receiver = user2;
        this.appointmentId = apptId;
        this.item1 = item1;
        this.item2 = item2;
    }

    //two items(two way)
    public TransactionTicket(Item item1, String time, String user1, String user2, String apptId) {
        this.time = time;
        this.proposer = user1;
        this.receiver = user2;
        this.appointmentId = apptId;
        this.item1 = item1;
    }


    public String getTime(){
        return time;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public String getProposer() {
        return proposer;
    }

    public String getReceiver() {
        return receiver;
    }

    public boolean getIsUser1Confirmed(){
        return isUser1Confirmed;
    }

    public boolean getIsUser2Confirmed(){
        return isUser2Confirmed;
    }

    public boolean confirm(String username, boolean isUserConfirmed) {
        if (username.equals(proposer) && isUserConfirmed) {
            this.isUser1Confirmed = true;
            return true;
        }
        else if (username.equals(receiver) && isUserConfirmed) {
            this.isUser2Confirmed = true;
            return true;
        }
        else return false;
    }

    public void setIsCompleted(boolean new_isCompleted){
        this.isCompleted = new_isCompleted;
    }

    public boolean getIsCompleted(){
        return this.isCompleted;
    }

    @Override
    public String toString() {
        return time + "," + appointmentId  + "," + proposer  + "," + receiver  + "," + isUser1Confirmed  + "," + isUser2Confirmed
                + "," + isCompleted;
    }
}
