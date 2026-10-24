package programLogic.controllerCode;

import programLogic.model.Account;
import programLogic.model.TicketManager;

public class UserAddTicketController {

    public static void createTicket(TicketManager ticketManager, Account account) {
        System.out.print("Enter a brief description (~5 words or less): ");
        String shortDesc = InputValidator.readLine();
        System.out.print("Enter a detailed description (with no line breaks): ");
        String longDesc = InputValidator.readLine();

        ticketManager.createTicket(account, shortDesc, longDesc);

    }
}
