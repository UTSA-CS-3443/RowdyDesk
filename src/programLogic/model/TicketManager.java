package programLogic.model;

import java.util.ArrayList;

public class TicketManager {

    ArrayList<Ticket> activeTickets;
    ArrayList<Ticket> archivedTickets;

    String csvFilename = "programLogic/data/ticketsData.csv";

    private int currentIDNumber;

    public TicketManager() {
        activeTickets = new ArrayList<Ticket>();
        archivedTickets = new ArrayList<Ticket>();
        currentIDNumber = 0;
    }

    public int getCurrentIDNumber() {
        return currentIDNumber;
    }

    public void setCurrentIDNumber(int currentIDNumber) {
        this.currentIDNumber = currentIDNumber;
    }


    public void addTicket(Ticket ticket) {
        activeTickets.add(ticket);
    }

    public Ticket createTicket(Account userName, AdminAccount assignedTo, String shortDescription, String longDescription) {
        Ticket newTicket = new Ticket(currentIDNumber, userName, assignedTo, shortDescription, longDescription);
        currentIDNumber += 1;

        newTicket.setTicketStatus(Status.UNASSIGNED);
        addTicket(newTicket);
        return newTicket;
    }

    public Ticket findTicket(int id) {

        for(Ticket ticket: activeTickets) {
            if(ticket.getId() == id) {
                return ticket;
            }
        }

        return null;
    }

    public void assignTicket(Ticket ticket, AdminAccount assignTo) {
        ticket.assignedTo = assignTo;
        ticket.setTicketStatus(Status.INPROGRESS);
    }

    public void resolveTicket(Ticket ticket) {
        ticket.setTicketStatus(Status.RESOLVED);
        activeTickets.remove(ticket);
        archivedTickets.add(ticket);
    }
    public void deleteTicket(Ticket ticket) {
        ticket.setTicketStatus(Status.DELETED);
        activeTickets.remove(ticket);
        archivedTickets.add(ticket);
    }

    public void initializeData(){
        //Todo implement file scanning
    }

    public Ticket lineToTicket(String line) {
        //Todo split the line by the comma and then createTicket()
        return null;
    }

    public String ticketToLine(Ticket ticket) {
        //Todo create string based on ticket
        return "";
    }
 }
