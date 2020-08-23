package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class AppointmentList implements Serializable {
    private List<Appointment> appointmentList;
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
        return this.appointmentList;
    }


    public String getId(){
        return this.id;
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
        StringBuilder out = new StringBuilder();
        if (!appointmentList.isEmpty()) {
            for (Appointment app : appointmentList) {
                out.append(app.toString()).append("\n");
            }
            return out.toString();
        }
        return "Empty list.";
    }
}
