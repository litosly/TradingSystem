package usecases;

import entities.Item;
import entities.Appointment;
import entities.ClientUser;


public class TradeManager {
    private ClientUserManager clientUserManager;



    public TradeManager(ClientUserManager clientUserManager) {
          this.clientUserManager = clientUserManager;
    }

    public boolean burrowable(ClientUser clientUser) {
        if (clientUser.getNumBurrowed() < clientUser.getNumLent()) {
            return true;
        }
        return false;
    }

    public Appointment trade(Item item1, Item item2, String time, String address){
        String userId1 = item1.getOwnerName();
        String userId2 = item2.getOwnerName();
        return createAppointmentTwoItems(item1, item2, userId1, userId2, time, address);
    }


    public Appointment lend(Item item1, ClientUser user2, String time, String address){
        String userId1 = item1.getOwnerName();
        String userId2 = user2.getUserName();
        return createAppointmentOneItem(item1, userId1, userId2, time, address);
    }

    public Appointment borrow(Item item1, ClientUser user2, String time, String address){
        String userId1 = item1.getOwnerName();
        String userId2 = user2.getUserName();
        return createAppointmentOneItem(item1, userId1, userId2, time, address);

    }

    //creates instance of appointment with 2 items
    public Appointment createAppointmentTwoItems(Item item1, Item item2, String userid1, String userid2, String time, String address){
        return new Appointment(item1, item2, userid1, userid2, time, address);
    }

    //creates instance of appointment with 1 item
    public Appointment createAppointmentOneItem(Item item1, String userid1, String userid2, String time, String address){
        return new Appointment(item1, userid1, userid2, time, address);
    }
    }

