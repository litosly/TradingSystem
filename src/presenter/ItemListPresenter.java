package presenter;

import entities.ClientUser;
import entities.TransactionTicket;
import usecases.ClientUserManager;
import usecases.ItemListManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import usecases.ItemListManager.*;
import static gateway.FileReadAndWrite.LOGIN_PROMPT;

public class ItemListPresenter {
    private ItemListManager itemListManager;
    private ClientUserManager clientUserManager;

    public ItemListPresenter(ClientUserManager clientUserManager){
        this.clientUserManager = clientUserManager;
        this.itemListManager = new ItemListManager(clientUserManager);
    }

    public void printRecentTransactions(int numTransactions, ClientUser currentUser){
        ArrayList<TransactionTicket> recentTransactions = itemListManager.getRecentTransactions(numTransactions, currentUser);
        for (TransactionTicket transactionTicket: recentTransactions){
            System.out.println(transactionTicket.toString());
        }
    }


    public void printTradingPartners(int numPartners, ClientUser currentUser){
        HashMap<String, Integer> tradingPartners = itemListManager.getTradePartners(currentUser);
        List<Map.Entry<String, Integer>> greatest = itemListManager.findGreatest(tradingPartners, numPartners);
        System.out.println("Top "+ numPartners +" Trading Partners:");
        for (Map.Entry<String, Integer> entry : greatest){
            System.out.println(entry.getKey());
        }
    }

    public void showAllUserInventories() {
        for (ClientUser clientUser : clientUserManager.getClientUserList().getActiveUser()) {
            System.out.println(clientUser.toString());
            System.out.println(clientUser.getInventory().toString());
        }
    }

    public void printPendingTransactions() {
        List<TransactionTicket> ticketList = clientUserManager.getCurrentUser().getPendingTransaction().getTransactionTicketList();
        for (TransactionTicket ticket : ticketList){
            System.out.println(ticket.toString());
        }
    }
}
