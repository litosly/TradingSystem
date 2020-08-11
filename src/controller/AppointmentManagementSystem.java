package controller;

import entities.Appointment;
import entities.AppointmentList;
import entities.ClientUser;
import entities.Item;
import entities.User;
import usecases.ClientUserManager;

import java.util.ArrayList;

public class AppointmentManagementSystem {
    /*private AppointmentList appointments;

    ClientUserManager userManager;  //create base class userManager and use that in client user manager and admin user manager
    public AppointmentManagementSystem(AppointmentList appointments) {
        this.appointments=  appointments;
    }

    public boolean addToWishList(Item item){
        if(item !=null){
            userManager.getCurrentUser().addToWishList(item);
            return true;
        }
        return false;
    }

    //public User searchByName(String name){
    //for(ClientUser user: userManager.getClientUserList().getAllClientUser()){ //clientuser is empty idk what to do with it
    //if(user.getUserName().equalsIgnoreCase(name)){
    //return user;
    //}
    //}
    //return null;}

    public AppointmentList showUpdatedAppointmentList(){
        return appointments;
    }

//    public boolean confirmAppointment(String apptid, String time, String userNameToMeet){
//        if(apptid!=null && !apptid.equals("") && !userNameToMeet.equals("")){
//            //Appointment appointment = new Appointment(userManager.getCurrentUser().getUserName(), userNameToMeet, time, address, apptid);
//            //appointment.confirm();
//            //appointments.addToAppointment(appointment);
//            return true;
//        }
//        return false;
//    }

    public void addAppointment(String user1Id, String user2Id, String time, String address){
        Appointment app = new Appointment(user1Id, user2Id, time, address);
        appointments.addToAppointment(app);
    }

    public void editAppointment(String appid, String time, String address, String userName){
        Appointment app = appointments.getAppointment(appid);
        app.setTime(time);
        app.setAddress(address);
        if(userName.equals(app.getUsername1())){
            app.addUser1Edit();
        }
        else if(userName.equals(app.getUsername2())){
            app.addUser2Edit();
        }
    }

    public boolean updateAppointment(String apptid, String time,String address){
        for(Appointment app: appointments.getAppointmentList()){
            if(!app.getIsConfirmed()){
                app.setAddress(address);
                app.setTime(time);
                return true;
            }
        }
        return false;
    }

     */

}

