package programLogic.controllerCode;

import programLogic.model.*;

import javax.swing.text.DateFormatter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Starts the command-line interface and coordinates RowdyDesk workflows.
 */
public class rowdyDeskCLI {

    public static void main(String[] args) {
        Scanner scnr = new Scanner(System.in);
        AccountManager accountManager = null;
        TicketManager ticketManager = null;

        try {
            accountManager = AccountManager.getInstance();
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            ticketManager = TicketManager.getInstance();
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }

        assert accountManager != null;
        assert ticketManager != null;

        System.out.println("""
                Would you like to log in or create an account?
                [1] Log In
                [2] Create Account
                [3] Quit""");

        int createAccResponse = InputValidator.readIntNumberBetween(1,3, "Please enter a number between 1 and 3");

        Account loggedInAccount = null;
        if(createAccResponse == 1) {
            loggedInAccount = LoginScreenController.logIn(accountManager);
        } else if (createAccResponse == 2) {
            loggedInAccount = LoginScreenController.createAccount(accountManager);
        } else if (createAccResponse == 3) {
            System.out.println("Thank you for considering using RowdyDesk!");
            return;
        }

        assert loggedInAccount != null;

        String userChoice;

        if(loggedInAccount instanceof AdminAccount adminLoggedInAccount) {
            while (true) {
                System.out.println("""
                        Welcome to RowdyDeskAdmin! please select an option
                        [1] View all active tickets
                        [2] View you tickets
                        [3] Create new ticket
                        [4] View and edit ticket
                        [5] Edit your account info
                        [6] View all accounts
                        [7] Find and manage an account
                        [8] Add new account
                        [9] Quit""");

                userChoice = scnr.next();
                // add an option for log out
                if (userChoice.equals("1")) {
                    // prints out all tickets
                    System.out.println(AdminViewAllTicketsController.viewAllTickets(ticketManager));

                } else if (userChoice.equals("2")) {
                    // prints out tickets that user has
                    System.out.println(AdminViewAllTicketsController.viewAssignedTickets(adminLoggedInAccount));

                } else if (userChoice.equals("3")) {
                    // prompts user and creates a ticket
                    AdminAddTicketController.createTicket(ticketManager,accountManager,adminLoggedInAccount);

                } else if (userChoice.equals("4")) {
                    // prompts user and gives a variety of ticket edit options
                    AdminViewAllTicketsController.editTicket(ticketManager,accountManager);

                } else if (userChoice.equals("5")) {
                    // prompts user for edits to their own account
                    if(AdminManageAccountsController.editOwnAccount(adminLoggedInAccount, accountManager)) {
                        // they deleted their own account
                        System.out.println("Account is now deleted! logging out...");
                    }

                } else if (userChoice.equals("6")) {
                    // prints out all accounts
                    System.out.println(AdminManageAccountsController.viewAllAccounts(accountManager));

                } else if (userChoice.equals("7")) {
                    // searches for an account
                    AdminManageAccountsController.editAccount(accountManager);

                } else if (userChoice.equals("8")) {
                    // Straight up creates an account
                   AdminManageAccountsController.createAccount(accountManager);

                } else if (userChoice.equals("9")) {
                    // can you guess what this does?
                    System.out.println("Thank you for using RowdyDeskAdmin!");
                    break;

                } else {
                    System.out.println("Please enter a valid command!");
                }


            }
        } else {
            while (true) {
                System.out.println("""
                        
                        Welcome to RowdyDesk! please select an option
                        [1] View your tickets
                        [2] Create new ticket
                        [3] Edit your account info
                        [4] Quit
                        """);

                userChoice = scnr.next();
                scnr.nextLine();

                if (userChoice.equals("1")) {
                    // View tickets
                    UserDashboardController.viewUserTickets(loggedInAccount);
                } else if (userChoice.equals("2")) {
                    // Create ticket
                    UserAddTicketController.createTicket(ticketManager, loggedInAccount);
                } else if (userChoice.equals("3")) {
                    // Edit account
                    if(UserDashboardController.editUserAccount(accountManager, loggedInAccount)) {
                        // they deleted their own account
                        System.out.println("Account is now deleted! logging out...");
                    }
                } else if (userChoice.equals("4")) {
                    System.out.println("Thank you for using RowdyDesk!");
                    break;
                } else {
                    System.out.println("Please enter a valid command!");
                }

            }
        }
    }
}
