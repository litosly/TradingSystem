package gateway;

import usecases.ClientUserManager;
import usecases.ThresholdManager;

import java.io.*;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThresholdReadWrite {
    private static final Logger logger = Logger.getLogger(ClientUserReadWrite.class.getName());
    private static final Handler consoleHandler = new ConsoleHandler();
    ThresholdManager thresholdManager;

    public ThresholdManager createThresholdManagerFromFile(String path) throws ClassNotFoundException {
        readFromFile(path);
        return thresholdManager;
    }

    private void readFromFile(String path) throws ClassNotFoundException {

        try {
            InputStream file = new FileInputStream(path);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);

            thresholdManager = (ThresholdManager) input.readObject();
            input.close();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Cannot read from input.", ex);
        }
    }

    public void saveToFile(String path, ThresholdManager ThresholdManager) throws IOException {
        OutputStream file = new FileOutputStream(path);
        OutputStream buffer = new BufferedOutputStream(file);
        ObjectOutput output = new ObjectOutputStream(buffer);

        output.writeObject(thresholdManager);
        output.close();
    }
}
