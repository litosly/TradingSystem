package controller;

import presenter.PromptPresenter;
import usecases.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import static gateway.FileReadAndWrite.*;

// TODO: finish this
public class AppointmentSystem implements InputProcessable{
    private AppointmentManager appointmentManager;

    public AppointmentSystem(AppointmentManager appointmentManager){
        this.appointmentManager = appointmentManager;
    }
    public void run(){
        System.out.println("Welcome to the Appointment System! At any time, 'exit' to quit the system.");
        processInput(PromptPresenter.takeInput(APPOINTENTS_PROMPT));

    }

    @Override
    public void processInput(ArrayList<String> inputArray) {

    }
}
