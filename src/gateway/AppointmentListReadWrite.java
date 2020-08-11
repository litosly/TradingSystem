package gateway;

import entities.AdminUser;
import entities.Appointment;
import entities.AppointmentList;
import usecases.AdminUserManager;
import usecases.AppointmentManager;
import usecases.ClientUserManager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static gateway.FileReadAndWrite.APPOINTMENTS_FILE;
import static gateway.FileReadAndWrite.CLIENT_USER_FILE;

public class AppointmentListReadWrite {
    private static final Logger logger = Logger.getLogger(ClientUserReadWrite.class.getName());
    private static final Handler consoleHandler = new ConsoleHandler();
    AppointmentManager ap;

    /*public AppointmentManager createAppointmentListFromCSV(String appointmentListCSV) {
        List<Appointment> appointmentList = new ArrayList<>();
        String[] appointmentStringList = appointmentListCSV.split(System.lineSeparator());
        //System.out.println("LENGTH" + adminUserStringList.length);
        for(int i = 1; i< appointmentStringList.length; i++){
            //skip title
            //System.out.println(adminUserStringList[i]);

            Appointment appointment = createAppointment(appointmentStringList[i]);


            appointmentList.add(appointment);
        }
        return new AppointmentManager(new AppointmentList(appointmentList));
    }



    private Appointment createAppointment(String appointmentString){
        String [] appointmentStringArray = appointmentString.split(",");

        String appointmentId = appointmentStringArray[0];;
        String time = appointmentStringArray[1];
        String address = appointmentStringArray[2];;
        String userOneName = appointmentStringArray[3];;
        String userTwoName = appointmentStringArray[4];;
        boolean isConfirmed = Boolean.getBoolean(appointmentStringArray[5]); //boolean value of whether the appointment is confirmed
        boolean isUpdated = Boolean.getBoolean(appointmentStringArray[6]); //boolean value of whether the appointment is confirmed;
        int user1EditsCount = Integer.parseInt(appointmentStringArray[7]);
        int user2EditsCount = Integer.parseInt(appointmentStringArray[8]);

        return new Appointment(appointmentId,time,address,userOneName,userTwoName,isConfirmed,isUpdated,user1EditsCount,user2EditsCount);
    }

    public void addNewAppointmentToFile(Appointment appointment) {
        FileReadAndWrite.appendLineToFile(APPOINTMENTS_FILE, appointment.getId() + "," + appointment.getTime() + "," +
                appointment.getAddress() + "," + appointment.getUsername1() + "," + appointment.getUsername2() + "," + appointment.getIsConfirmed() +
                "," + appointment.getIsUpdated() + "," + appointment.getUser1EditsCount() + "," + appointment.getUser2EditsCount());
    }

     */

    public AppointmentManager createAppointmentManagerFromFile(String path) throws ClassNotFoundException {
        readFromFile(path);
        return ap;
    }

    private void readFromFile(String path) throws ClassNotFoundException {

        try {
            InputStream file = new FileInputStream(path);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);

            ap = (AppointmentManager) input.readObject();
            input.close();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Cannot read from input.", ex);
        }
    }

    public void saveToFile(String path, AppointmentManager appointmentManager) throws IOException {
        OutputStream file = new FileOutputStream(path);
        OutputStream buffer = new BufferedOutputStream(file);
        ObjectOutput output = new ObjectOutputStream(buffer);

        output.writeObject(appointmentManager);
        output.close();
    }

}
