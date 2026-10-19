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
}
