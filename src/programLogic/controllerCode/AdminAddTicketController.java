package programLogic.controllerCode;

import programLogic.model.Account;
import programLogic.model.AccountManager;
import programLogic.model.AdminAccount;
import programLogic.model.TicketManager;
import programLogic.model.Ticket;

/**
 * Handles the command-line workflow for administrators creating support tickets.
 */
public class AdminAddTicketController {

    public static void createTicket(TicketManager ticketManager, AccountManager accountManager, AdminAccount thisAccount) {

        System.out.println("Please enter the username of the user");
        Account user;
        while(true) {
            user = accountManager.findAccount(InputValidator.readString());


            if(user != null) {
                break;
            }
            System.out.println("Error, user not found. Enter 'guest' if user is unknown.");
        }

        System.out.println("Please enter a short description of the issue: ");
        String shortDesc = InputValidator.readLine();


        System.out.println("Now you may enter a more specific description of the issue");
        String longDesc = InputValidator.readLine();

        Ticket newTicket = ticketManager.createTicket(user, shortDesc, longDesc);
        System.out.println("Ticket has been successfully created");

        System.out.println("Assign this ticket to you?");
        boolean assignToThisAdmin = InputValidator.readBooleanValue("please enter either Yes or No");

        if (assignToThisAdmin) {
            ticketManager.assignTicket(newTicket, thisAccount);
            System.out.println("Ticket has been assigned to you!");
        }
    }
}
