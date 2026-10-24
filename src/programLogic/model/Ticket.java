package programLogic.model;

import java.util.Date;

/**
 * Represents a support ticket, including its requester, assignment, status, and descriptions.
 */
public class Ticket {

    private final int id;
    private Status ticketStatus;
    Account user;
    AdminAccount assignedTo;
    String shortDescription;
    String longDescription;
    private final Date timeCreated;

    /**
     * Creates a ticket with its requester, assignment, content, status, and creation time.
     */
    public Ticket(int id, Account user, AdminAccount assignedTo, String shortDescription, String longDescription, Status ticketStatus, Date timeCreated) {
        this.id = id;
        this.user = user;
        this.assignedTo = assignedTo;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.ticketStatus = ticketStatus;
        this.timeCreated = timeCreated;
    }


    /** Changes the account that submitted this ticket. */
    public void setUser(Account user) {
        this.user = user;
    }

    /** Changes the administrator assigned to this ticket. */
    public void setAssignedTo(AdminAccount assignedTo) {
        this.assignedTo = assignedTo;
    }

    /** Updates the ticket's short description. */
    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    /** Updates the ticket's detailed description. */
    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    /** @return the account that submitted this ticket */
    public Account getUser() {
        return user;
    }

    /** @return the administrator assigned to this ticket, or {@code null} */
    public AdminAccount getAssignedTo() {
        return assignedTo;
    }

    /** @return the ticket's short description */
    public String getShortDescription() {
        return shortDescription;
    }

    /** @return the ticket's detailed description */
    public String getLongDescription() {
        return longDescription;
    }

    /** @return the unique ticket identifier */
    public int getId() {
        return id;
    }

    /** @return the ticket creation time */
    public Date getTimeCreated() {
        return timeCreated;
    }

    /** @return the ticket's current lifecycle status */
    public Status getTicketStatus() {
        return ticketStatus;
    }

    /** Updates the ticket's lifecycle status. */
    public void setTicketStatus(Status ticketStatus) {
        this.ticketStatus = ticketStatus;
    }


}
