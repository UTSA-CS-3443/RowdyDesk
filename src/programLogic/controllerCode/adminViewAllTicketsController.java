package programLogic.controllerCode;

import programLogic.model.Ticket;
import programLogic.model.TicketManager;

public class adminViewAllTicketsController {


    public static String viewAllTickets(TicketManager ticketManager) {
        String outputString = "ID #   Short Desc          Status     User     Assigned To\n";

        for(Ticket ticket: ticketManager.getActiveTickets()) {
            outputString += String.format("%d %s %s   %s   ",
                    ticket.getId(),
                    ticket.getShortDescription(),
                    ticket.getTicketStatus(),
                    ticket.getUser().getUserName());
            if(ticket.getAssignedTo() != null) {
                outputString += ticket.getAssignedTo().getUserName();
            }
            outputString += "\n";
        }

        return outputString;
    }
}
