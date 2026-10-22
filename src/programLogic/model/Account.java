package programLogic.model;

import java.util.ArrayList;

public class Account {

    public Account(String userName, String displayName, int id, String password) {
        this.userName = userName;
        this.displayName = displayName;
        this.id = id;
        this.password = password;
    }

    private String userName;
    private String displayName;
    private int id;
    private String password;
    private ArrayList<Ticket> tickets = new ArrayList<Ticket>();

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setTickets(ArrayList<Ticket> tickets) {
        this.tickets = tickets;
    }

    public String getUserName() {
        return userName;
    }
    public String getDisplayName() {
        return displayName;
    }
    public int getId() {
        return id;
    }
    protected String getPassword() {
        return password;
    }
    public ArrayList<Ticket> getTickets() {
        return tickets;
    }

    public boolean isAdminAccount() {
        return this instanceof AdminAccount;
    }
}
