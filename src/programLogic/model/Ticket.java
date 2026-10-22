package programLogic.model;

import java.util.Date;

public class Ticket {

    private final int id;
    private Status ticketStatus;
    Account user;
    AdminAccount assignedTo;
    String shortDescription;
    String longDescription;
    private final Date timeCreated;

    public Ticket(int id, Account user, AdminAccount assignedTo, String shortDescription, String longDescription, Status ticketStatus, Date timeCreated) {
        this.id = id;
        this.user = user;
        this.assignedTo = assignedTo;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.ticketStatus = ticketStatus;
        this.timeCreated = timeCreated;
    }


    public void setUser(Account user) {
        this.user = user;
    }

    public void setAssignedTo(AdminAccount assignedTo) {
        this.assignedTo = assignedTo;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public Account getUser() {
        return user;
    }

    public AdminAccount getAssignedTo() {
        return assignedTo;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public int getId() {
        return id;
    }

    public Date getTimeCreated() {
        return timeCreated;
    }

    public Status getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(Status ticketStatus) {
        this.ticketStatus = ticketStatus;
    }


}
