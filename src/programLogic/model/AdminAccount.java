package programLogic.model;

import java.util.ArrayList;

/**
 * Represents an administrator account and its assigned support tickets.
 */
public class AdminAccount extends Account {

    private ArrayList<Ticket> assignedTickets;

    /**
     * Creates an administrator account with no assigned tickets.
     *
     * @param id unique account identifier
     * @param displayName name shown to other users
     * @param userName login username
     * @param password account password
     */
    public AdminAccount(int id, String displayName, String userName, String password) {
        super(id, displayName, userName, password);
        assignedTickets = new ArrayList<Ticket>();
    }

    /** @return the tickets assigned to this administrator */
    public ArrayList<Ticket> getAssignedTickets() {
        return assignedTickets;
    }

    /** Replaces this administrator's assigned ticket list. */
    public void setAssignedTickets(ArrayList<Ticket> assignedTickets) {
        this.assignedTickets = assignedTickets;
    }

    /** Adds a ticket to this administrator's assigned ticket list. */
    public void addTicket(Ticket ticket) {
        assignedTickets.add(ticket);
    }

    /**
     * Removes a ticket from this administrator's assigned ticket list.
     *
     * @return {@code true} if the ticket was present and removed
     */
    public boolean removeTicket(Ticket ticket) {
        return assignedTickets.remove(ticket);
    }


}
