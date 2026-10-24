package programLogic.controllerCode;

import programLogic.model.Account;
import programLogic.model.AccountManager;
import programLogic.model.Ticket;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Handles user ticket display and account-management actions.
 */
public class UserDashboardController {

    public static void viewUserTickets(Account account) {
        ArrayList<Ticket> tickets = account.getTickets();
        System.out.printf("You have %d tickets:\n___________________________________________\n", tickets.size());
        for(Ticket t : tickets) {
            System.out.printf("%s\n%s | Status: %s\n%s\n",
                    t.getShortDescription(),
                    new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(t.getTimeCreated()),
                    t.getTicketStatus(),
                    t.getLongDescription()
            );
            System.out.println("___________________________________________");
        }
    }

    public static boolean editUserAccount(AccountManager accountManager, Account account) {
        int response;
        while (true) {
            System.out.println("What would you like to do with your account, " + account.getUserName() + "?");
            System.out.println("""
                [1] Edit Username
                [2] Edit Display Name
                [3] Change Password
                [4] Delete Account
                [5] Do nothing and quit""");
            response = InputValidator.readIntNumberBetween(1, 5, "Please enter a valid number between 1 and 5");
            if (response == 1) {
                System.out.println("Please enter your new username");
                String userName = InputValidator.readString();
                account.setUserName(userName);
                System.out.println("Updated username to " + userName);
            } else if (response == 2) {
                System.out.println("Please enter your new display name");
                String displayName = InputValidator.readString();
                account.setDisplayName(displayName);
                System.out.println("Updated display Name to " + displayName);
            } else if (response == 3) {
                System.out.println("Please enter your old password, followed by your new password");
                String oldPass = InputValidator.readString();
                String newPass = InputValidator.readString();
                if (accountManager.changePassword(account, oldPass, newPass)) {
                    System.out.println("password change successful!");
                } else {
                    System.out.println("old password is incorrect");
                }
            } else if (response == 4) {
                System.out.println("Are you sure your want to delete your own account, " + account.getUserName() + "?");
                boolean confirm = InputValidator.readBooleanValue("Enter yes or no");
                if (confirm) {
                    accountManager.deleteAccount(account);
                    System.out.println("Account Deleted");
                    return true;
                } else {
                    System.out.println("Account NOT deleted");
                }
            } else if (response == 5) {
                return false;
            }
        }
    }
}
