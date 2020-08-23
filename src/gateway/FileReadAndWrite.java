package gateway;

import java.io.*;

// TODO: document it
public class FileReadAndWrite {

    public final static String CLIENT_USER_FILE = "./src/database/client_users.ser";
    public final static String ADMIN_USER_FILE = "./src/database/admin_users.ser";
    public final static String LOGIN_PROMPT = "database/loginPrompt.txt";
    public final static String SIGNUP_PROMPT = "database/signUpPrompt.txt";
    public final static String APPOINTENTS_PROMPT = "database/AppointmentUserSystemPrompt.txt";
    public final static String CLIENT_USER_SYSTEM_PROMPT = "database/clientUserSystemPrompt.txt";
    public final static String ADMIN_USER_SYSTEM_PROMPT = "database/adminUserSystemPrompt.txt";
    public final static String INITIAL_ADMIN_USER_SYSTEM_PROMPT = "database/initialAdminUserPrompt.txt";
    public final static String CREATE_ADMIN_PROMPT = "database/createAdminPrompt.txt";
    public final static String TRADE_SYSTEM_PROMPT = "database/tradingSystemPrompt.txt";
    public final static String REQUEST_TO_ADD_ITEM_PROMPT = "database/requestToAddItemPrompt.txt";
    public final static String TRADE_SET_UP_PROMPT = "database/tradeSetUpPrompt.txt";

    // Threshold manager
    public final static String THRESHOLDMANAGER_FILE = "./phase1/src/database/thresholds.ser";

    //Admin System Prompts
    //Threshold Prompts
    public final static String THRESHOLD_PROMPT = "database/threshholdPrompt.txt";
    public final static String CHANGE_THRESHOLD_PROMPT = "database/changeThresholdPrompt.txt";
    public final static String CHANGE_USER_THRESHOLD_PROMPT = "database/changeUserThresholdPrompt.txt";
    public final static String ADD_USER_THRESHOLD_PROMPT = "database/addUserThresholdPrompt.txt";
    public final static String REMOVE_THRESHOLD_PROMPT = "database/removeThresholdPrompt.txt";
    public final static String FREEZE_PROMPT = "database/freeze_prompt.txt";
    public final static String UNFREEZE_PROMPT = "database/unfreeze_prompt.txt";
    //Transactions Prompts
    public final static String GET_PENDING_TRANSACTION_PROMPT = "database/getPendingTransactionsPrompt.txt";

    //public final static String APPOINTMENTS_FILE = "appointments.csv";
    public final static String APPOINTMENTS_FILE = "./src/database/appointments.ser";
    public final static String PENDING_TRANSACTIONS_FILE = "database/pending_transactions.csv";
    public final static String HISTORY_TRANSACTIONS_FILE = "database/history_transactions.csv";

    // Visitor System Prompts
    public final static String VISITOR_PROMPT = "database/VISITOR_PROMT.txt";

    private final static String filePath = "./src/";

    //reference https://stackoverflow.com/a/4716623
    public static String readFromFile(String fileName){
        try(BufferedReader br = new BufferedReader(new FileReader(filePath + fileName))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    //reference:https://howtodoinjava.com/java/io/java-append-to-file/
    public static void appendLineToFile(String fileName, String text){
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(
                    new FileWriter(filePath + fileName, true)  //Set true for append mode
            );
            writer.newLine();
            writer.write(text);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }




}
