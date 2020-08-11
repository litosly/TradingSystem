package usecases;

import entities.TransactionTicket;
import entities.TransactionTicketList;

public class TransactionTicketManager {
    private TransactionTicketList historyTransactionTicketList;
    // TODO pendingTransactionTicketList to pendingTransactionTicketList
    private TransactionTicketList pendingTransactionTicketList;

    public TransactionTicketManager(TransactionTicketList historyTransactionTicketList, TransactionTicketList pendingTransactionTicketList) {
        this.historyTransactionTicketList = historyTransactionTicketList;
        this.pendingTransactionTicketList = pendingTransactionTicketList;
    }

    public void removeExpiredTicket(TransactionTicket ticket) {
        pendingTransactionTicketList.removeTransactionTicketList(ticket);
    }

    public void addTicketToHistory(TransactionTicket ticket) {
        historyTransactionTicketList.addToTransactionTicketList(ticket);
    }

    public void addTicketToPending(TransactionTicket ticket) {
        pendingTransactionTicketList.addToTransactionTicketList(ticket);
    }

    // TODO Very similar you can prob modularise it
    public TransactionTicketList getHistoryTransactionTicketByUser(String username){
        TransactionTicketList historyTransactionTicketList = new TransactionTicketList();
        for(TransactionTicket ticket: this.historyTransactionTicketList.getTransactionTicketList()){
            if(ticket.getProposer().equalsIgnoreCase(username) || ticket.getReceiver().equalsIgnoreCase(username)){
                historyTransactionTicketList.getTransactionTicketList().add(ticket);
            }
        }
        return historyTransactionTicketList;
    }

    public TransactionTicketList getPendingTransactionTicketByUser(String username){
        TransactionTicketList pendingTicketList = new TransactionTicketList();
        for(TransactionTicket ticket: this.pendingTransactionTicketList.getTransactionTicketList()){
            if(ticket.getProposer().equalsIgnoreCase(username) || ticket.getReceiver().equalsIgnoreCase(username)){
                pendingTicketList.getTransactionTicketList().add(ticket);
            }
        }
        return pendingTicketList;
    }



    // means that both user1 and user2 had physically attended the meeting and completed the trade.
    public boolean completeTransaction(TransactionTicket ticket){
        //TODO: finish this method
        boolean user1ConfirmStatus = ticket.getIsUser1Confirmed();
        boolean user2ConfirmStatus = ticket.getIsUser2Confirmed();
        //both users showed up-> transaction completed
        if (user1ConfirmStatus && user2ConfirmStatus){
            ticket.setIsCompleted(true);
            return true;
        }
        else{
            //transaction is not complete
            return false;
        }
    }

}
