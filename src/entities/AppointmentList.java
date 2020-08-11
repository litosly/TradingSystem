package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class AppointmentList implements Serializable {
    private List<Appointment> appointmentList;
    // TODO: what is the point of this id
    private String id;

    public AppointmentList(){
        this.appointmentList = new ArrayList<>();
        id = UUID.randomUUID().toString();
    }

    public AppointmentList(List<Appointment> appointmentList){
        this.appointmentList = appointmentList;
        this.id = UUID.randomUUID().toString();
    }


    public AppointmentList(String id, List<Appointment> appointmentList){
        this.appointmentList = appointmentList;
        this.id = id;
    }

    public List<Appointment> getAppointmentList(){
        return appointmentList;
    }

    public String getId(){
        return id;
    }

    public void addToAppointment(Appointment appointment){
        appointmentList.add(appointment);
    }

    public Appointment getAppointment(String appid){
        for (Appointment app: appointmentList){
            if(app.getId().equals(appid)){
                return app;
            }
        }
        return null;
    }

    public String toString() {
        if (!appointmentList.isEmpty()) {
            for (Appointment app : appointmentList) {
                return app.toString();
            }
        }
        return "Empty list.";
    }
}
