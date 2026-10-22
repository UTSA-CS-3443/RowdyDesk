package programLogic.controllerCode;

import programLogic.model.*;

import java.util.Scanner;

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

        boolean loggedIn = false;
        String username;
        String password;

        Account loggedInAccount = null;
        while(!loggedIn) {
            try {
                System.out.println("Please Enter your Username:");
                username = scnr.next();
                System.out.println("Please Enter your Password:");
                password = scnr.next();

                loggedInAccount = accountManager.login(username,password);

                System.out.println("Successfully logged in! Hello "+username+"!");
                loggedIn = true;

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        String userChoice;

        if(loggedInAccount instanceof AdminAccount adminLoggedInAccount) {
            while (true) {
                System.out.println("""
                        Welcome to RowdyDeskAdmin! please select an option
                        [1] view all active tickets
                        [2] view tickets assigned to you
                        [3] create new ticket
                        [4] edit ticket
                        [5] edit your account info
                        [6] view all accounts
                        [7] find and manage an account
                        [8] add new account
                        [9] quit""");

                userChoice = scnr.next();

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
                        [1] view your tickets
                        [2] create new ticket
                        [3] edit your account info
                        [4] quit
                        """);

                userChoice = scnr.next();

                if (userChoice.equals("1")) {
                    //get list of tickets of user
                    // loop thru and print out
                } else if (userChoice.equals("2")) {
                    // create ticket
                } else if (userChoice.equals("3")) {
                    // Get valid info
                    // create with function
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
