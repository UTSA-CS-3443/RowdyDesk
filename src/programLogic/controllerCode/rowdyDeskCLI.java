package programLogic.controllerCode;

import programLogic.model.*;

public class rowdyDeskCLI {

    public static void main(String[] args) {
        AccountManager accountManager = new AccountManager();
        TicketManager ticketManager = new TicketManager(accountManager);

        try {
            accountManager.loadAccounts();
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }

//        try {
//            ticketManager.initializeData();
//        }
//        catch(Exception e) {
//            System.out.println(e.getMessage());
//        }
    }
}
