package programLogic.model;

import java.util.ArrayList;

/**
 * Represents a RowdyDesk user account and the tickets submitted by that user.
 */
public class Account {
    public Account(int id, String userName, String password, String displayName, String email, String phone, String profilePictureFilePath) {
        this.userName = userName;
        this.displayName = displayName;
        this.id = id;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.profilePictureFilePath = profilePictureFilePath;
    }

    /**
     * Creates an account with the supplied identity and credentials.
     *
     * @param id unique account identifier
     * @param displayName name shown to other users
     * @param userName login username
     * @param password account password
     */


    private String userName;
    private String displayName;
    private int id;
    private String password;
    private ArrayList<Ticket> tickets = new ArrayList<Ticket>();
    private String phone;
    private String email;
    private String profilePictureFilePath;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePictureFilePath() {
        return profilePictureFilePath;
    }

    public void setProfilePictureFilePath(String profilePictureFilePath) {
        this.profilePictureFilePath = profilePictureFilePath;
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
