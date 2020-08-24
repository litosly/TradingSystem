package gateway;

import entities.AppointmentList;
import entities.ClientUserList;
import entities.ItemList;
import entities.TransactionTicketList;
import usecases.AppointmentManager;
import usecases.ClientUserManager;
import usecases.ItemListManager;
import usecases.TransactionTicketManager;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static gateway.FileReadAndWrite.*;

public class ClientUserReadWrite {
    private static final Logger logger = Logger.getLogger(ClientUserReadWrite.class.getName());
    private static final Handler consoleHandler = new ConsoleHandler();
    ClientUserManager cm;


    public ClientUserManager createClientUserManagerFromFile(String path) throws ClassNotFoundException {
        readFromFile(path);
        return cm;
    }

    private void readFromFile(String path) throws ClassNotFoundException {

        try {
            InputStream file = new FileInputStream(path);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);

            cm = (ClientUserManager) input.readObject();
            input.close();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Cannot read from input.", ex);
        }
    }

    public static void saveToFile(String path, ClientUserManager clientUserManager) throws IOException {
        OutputStream file = new FileOutputStream(path);
        OutputStream buffer = new BufferedOutputStream(file);
        ObjectOutput output = new ObjectOutputStream(buffer);

        output.writeObject(clientUserManager);
        output.close();
    }

    public Map<String, Integer> readThresholdsFromCSV(String path) throws IOException {
        Map<String, Integer> thresholds = new HashMap<>();
        BufferedReader br = new BufferedReader(new FileReader(path));
        String line;
        while((line=br.readLine())!=null){
            String str[] = line.replaceAll("\\s","").split(",");
            thresholds.put(str[0], Integer.parseInt(str[1]));
        }
//        System.out.println(thresholds);
        return thresholds;
    }

}
