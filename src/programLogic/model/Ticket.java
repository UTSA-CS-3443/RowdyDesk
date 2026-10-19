package programLogic.model;

public class Ticket {

    private final int id;
    private Status ticketStatus;
    Account user;
    AdminAccount assignedTo;
    String shortDescription;
    String longDescription;


    public Ticket(int id, Account user, AdminAccount assignedTo, String shortDescription, String longDescription) {
        this.id =id;
        this.user =user;
        this.assignedTo =assignedTo;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;

        ticketStatus = Status.PENDING;
    }

    public int getId() {
        return id;
    }

    public Status getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(Status ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

}
