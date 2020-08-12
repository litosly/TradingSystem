package usecases;

import entities.*;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientUserManager implements Serializable {
    private static final Logger logger = Logger.getLogger(ClientUserManager.class.getName());
    private ClientUserList clientUserList;
    private ClientUser currentUser;

    public ClientUserManager(ClientUserList clientUserList){
        this.clientUserList = clientUserList;
    }

    public ClientUserManager(){
        this.clientUserList = new ClientUserList();
    }

    public ClientUserList getClientUserList() {
        return clientUserList;
    }

    public boolean login(String userName, String password){
        for(ClientUser clientUser : clientUserList.getAllClientUser()){
            if(clientUser.getUserName().equals(userName)){
                if(clientUser.passwordMatch(password)){
                    currentUser = clientUser;
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    public boolean createUserAccount(String username, String password){
        for(ClientUser clientUser: clientUserList.getAllClientUser()){
            if(clientUser.getUserName().equalsIgnoreCase(username)){
                return false;
            }
        }
        ClientUser clientUser = new ClientUser(username, password);
        clientUserList.addToActiveUser(clientUser);
        clientUserList.addToAllClientUser(clientUser);
        //log the act of signup and display it on screen
        logger.log(Level.INFO, "Created a new client user");
        currentUser = clientUser;
        return true;
    }


    public void showPendingUsers() {
        if (clientUserList.getPendingUser().isEmpty()) {
            System.out.println("No pending users");
        } else {
            for (ClientUser user: clientUserList.getPendingUser()) {
                System.out.println(user.toString());
            }
        }
    }

    public void printAllUsers() {
        if (clientUserList.getAllClientUser().isEmpty()) {
            System.out.println("No client users");
        } else {
            for (ClientUser user: clientUserList.getAllClientUser()) {
                System.out.println(user.toString());
                System.out.println(user.getPendingAppointments().toString());
            }
        }
    }


    // For Admin Account
    public void changeUserStatusTo(ClientUser user, String status) {
        user.setAccountStatus(status);
    }

    public ClientUser getCurrentUser(){
        return this.currentUser;
    }

    public boolean isExist(String username) {
        AtomicBoolean found = new AtomicBoolean(false);
        clientUserList.getAllClientUser().forEach((item) -> {
            if (item.getUserName().equals(username))
                found.set(true);
        });
        return found.get();
    }

    public ClientUser getUserByUsername(String user){
        List<ClientUser> all_users = clientUserList.getAllClientUser();
        for (ClientUser to_find: all_users){
            if (to_find.getUserName().equals(user)){
                return to_find;
            }
        }
        return null;
    }


}
