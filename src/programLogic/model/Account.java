package programLogic.model;

import java.util.ArrayList;

/**
 * Represents a RowdyDesk user account and the tickets submitted by that user.
 */
public class Account {

    /**
     * Creates an account with the supplied identity and credentials.
     *
     * @param id unique account identifier
     * @param displayName name shown to other users
     * @param userName login username
     * @param password account password
     */
    public Account( int id, String displayName, String userName, String password) {
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

    /** Updates the account's display name. */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /** Updates the account's login username. */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /** Updates the account password for use by account management. */
    protected void setPassword(String password) {
        this.password = password;
    }

    /** Replaces the tickets submitted by this account. */
    public void setTickets(ArrayList<Ticket> tickets) {
        this.tickets = tickets;
    }

    /** @return the account's login username */
    public String getUserName() {
        return userName;
    }
    /** @return the name displayed for this account */
    public String getDisplayName() {
        return displayName;
    }
    /** @return the unique account identifier */
    public int getId() {
        return id;
    }
    /** @return the account password for use by account management */
    protected String getPassword() {
        return password;
    }
    /** @return the tickets submitted by this account */
    public ArrayList<Ticket> getTickets() {
        return tickets;
    }

    /** Adds a submitted ticket to this account's ticket list. */
    public void addTicket(Ticket t) {
        getTickets().add(t);
    }

    /** @return {@code true} when this account is an administrator account */
    public boolean isAdminAccount() {
        return this instanceof AdminAccount;
    }
}
