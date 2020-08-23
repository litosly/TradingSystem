package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TransactionTicketList implements Serializable {
    private List<TransactionTicket> transactionTicketList;
    private String id;

    public TransactionTicketList(){
        this.transactionTicketList = new ArrayList<>();
        this.id = UUID.randomUUID().toString();
    }

    public TransactionTicketList(List<TransactionTicket> transactionTickets){
        this.transactionTicketList = transactionTickets;
        this.id = UUID.randomUUID().toString();
    }
    public TransactionTicketList(String id, List<TransactionTicket> transactionTickets){
        this.transactionTicketList = transactionTickets;
        this.id = id;
    }

    public List<TransactionTicket> getTransactionTicketList(){
        return this.transactionTicketList;
    }

    public String getId(){
        return this.id;
    }

    public void addToTransactionTicketList(TransactionTicket transactionTicket){
        this.transactionTicketList.add(transactionTicket);
    }

    // David's Code (I think we need this to remove Expired tickets)
    public void removeTransactionTicketList(TransactionTicket transactionTicket){
        this.transactionTicketList.remove(transactionTicket);
    }

    

}
