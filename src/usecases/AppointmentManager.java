package usecases;


import entities.Appointment;
import entities.AppointmentList;
import entities.*;
import usecases.*;
import entities.TransactionTicket;
import entities.TransactionTicketList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AppointmentManager {
    private ClientUserManager clientUserManager;
    private ThresholdManager thresholdmanager;
    private AppointmentList appointmentList;
    public AppointmentManager(ClientUserManager clientUserManager){
        this.clientUserManager = clientUserManager;
    }

    // changing Time/address/Time and Address
    // For changing both Time and Address, use format: "new time,new address"
    // userId is the person that is changing
    public boolean updateAppointment(String apptId, String userId, String fieldType, String newVal){
        for(Appointment appointment: appointmentList.getAppointmentList()){
           if (appointment.getId().equals(apptId)){
               //check if user 1 edits count is less than 3
               if (userId.equals(appointment.getUsername1()) && appointment.getUser1EditsCount() <3){
                   switch (fieldType) {
                       case "Time":
                           appointment.setTime(newVal);
                           appointment.incrementUserEditsCount(userId);
                           return true;//update success

                       case "Address":
                           appointment.setAddress(newVal);
                           appointment.incrementUserEditsCount(userId);
                           return true; //update success

                       case "TimeAndAddress":
                           String time = newVal.substring(0, newVal.indexOf(","));
                           String address = newVal.substring(newVal.indexOf(",") + 1);
                           appointment.setTime(time);
                           appointment.setAddress(address);
                           appointment.incrementUserEditsCount(userId);
                           return true; //update success
                       default:
                           return false; //invalid field type
                   }
               }
               //check if user 2 edits count is less than 3
               else if (userId.equals(appointment.getUsername2()) && appointment.getUser2EditsCount() <3){
                   switch (fieldType) {
                       case "Time":
                           appointment.setTime(newVal);
                           appointment.incrementUserEditsCount(userId);
                           return true;//update success

                       case "Address":
                           appointment.setAddress(newVal);
                           appointment.incrementUserEditsCount(userId);
                           return true; //update success

                       case "TimeAndAddress":
                           String time = newVal.substring(0, newVal.indexOf(","));
                           String address = newVal.substring(newVal.indexOf(",") + 1);
                           appointment.setTime(time);
                           appointment.setAddress(address);
                           appointment.incrementUserEditsCount(userId);
                           return true; //update success
                       default:
                           return false; //invalid field type
                   }
               }
               else{
                   return false; //user exceeded maximum edits
               }

           }
       }
       return false; //apptId invalid, update unsuccessful
    }

    public void addAppointmentOneItem(Item item1, String userid1, String userid2, String time, String address){
        Appointment newAppointment = new Appointment(item1, userid1, userid2, time, address);
        appointmentList.addToAppointment(newAppointment);
    }
    public void addAppointmentTwoItems(Item item1, Item item2, String userid1, String userid2, String time, String address){
        Appointment newAppointment = new Appointment(item1, item2, userid1, userid2, time, address);
        appointmentList.addToAppointment(newAppointment);
    }

    public TransactionTicket createTransactionTicket(Appointment appointment){
        TransactionTicket transactionTicket = new TransactionTicket(appointment.getItem1(), appointment.getItem2(), appointment.getTime(), appointment.getUsername1(), appointment.getUsername2(), appointment.getId());
        return transactionTicket;
    }

    public AppointmentList getAppointmentList(){
        return appointmentList;
    }

    public AppointmentList getAppointmentByUser(String username){
        AppointmentList appointmentList = new AppointmentList();
        for(Appointment appointment: this.appointmentList.getAppointmentList()){
            if(appointment.getUsername1().equalsIgnoreCase(username) || appointment.getUsername2().equalsIgnoreCase(username)){
                appointmentList.getAppointmentList().add(appointment);
            }
        }
        return appointmentList;
    }




}
