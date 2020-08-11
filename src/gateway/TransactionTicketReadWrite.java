package gateway;

import entities.TransactionTicket;
import entities.TransactionTicketList;
import usecases.TransactionTicketManager;
import static gateway.FileReadAndWrite.*;

import java.util.ArrayList;
import java.util.List;

public class TransactionTicketReadWrite {

    // TODO: Apparently TransactionTicketManger takes to lists? I need to prompts?
    /*public TransactionTicketList createTransactionTicketListFromCSV(String ticketListCSVFile) {
        List<TransactionTicket> transactionList = new ArrayList<>();
        String[] transactionTickets = ticketListCSVFile.split(System.lineSeparator());
        for (int i = 1; i < transactionTickets.length; i++) {
            //skip title
            TransactionTicket ticket = createTransactionTicket(transactionTickets[i]);
            transactionList.add(ticket);
        }
        return new TransactionTicketList(transactionList);
    }

    public TransactionTicketManager createTransactionTicketManagerFromCSV() {
        TransactionTicketList history = createTransactionTicketListFromCSV(HISTORY_TRANSACTIONS_FILE);
        TransactionTicketList pendingTrans = createTransactionTicketListFromCSV(PENDING_TRANSACTIONS_FILE);
        return new TransactionTicketManager(history,pendingTrans);
    }

    public TransactionTicket createTransactionTicket(String ticketString) {
        String[] ticketProperties = ticketString.split(",");
        String time = ticketProperties[0];
        boolean isOneWay = Boolean.parseBoolean(ticketProperties[1]);
        String user1 = ticketProperties[2];
        String user2 = ticketProperties[3];
        int apptId = Integer.parseInt(ticketProperties[4]);
        return new TransactionTicket(time, isOneWay, user1, user2, apptId);
    }

    public void addTransactionTicketToFile(TransactionTicket transactionTicket, String fileName) {
        FileReadAndWrite.appendLineToFile(fileName, transactionTicket.getTime() + "," + transactionTicket.getIsOneWay()+ "," +
                transactionTicket.getAppointmentId()+ "," +transactionTicket.getProposer()+ "," +transactionTicket.getReceiver()+ "," +transactionTicket.getIsUser1Confirmed()
                + "," +transactionTicket.getIsUser2Confirmed()+ "," +transactionTicket.getIsCompleted());
    }
    
     */
}
