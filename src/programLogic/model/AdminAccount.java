package programLogic.model;

import java.util.ArrayList;

public class AdminAccount extends Account {

    private ArrayList<Ticket> assignedTickets;

    public AdminAccount(int id, String displayName, String userName, String password) {
        super(id, displayName, userName, password);
        assignedTickets = new ArrayList<Ticket>();
    }

    public ArrayList<Ticket> getAssignedTickets() {
        return assignedTickets;
    }

    public void setAssignedTickets(ArrayList<Ticket> assignedTickets) {
        this.assignedTickets = assignedTickets;
    }

    public void addTicket(Ticket ticket) {
        assignedTickets.add(ticket);
    }

    public boolean removeTicket(Ticket ticket) {
        return assignedTickets.remove(ticket);
    }


}
