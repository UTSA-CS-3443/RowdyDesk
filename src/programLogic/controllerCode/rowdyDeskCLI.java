package programLogic.controllerCode;

import programLogic.model.*;

import java.util.Scanner;

public class rowdyDeskCLI {

    public static void main(String[] args) {
        AccountManager accountManager = new AccountManager();
        TicketManager ticketManager = new TicketManager(accountManager);
        Scanner scnr = new Scanner(System.in);

        try {
            accountManager.loadAccounts();
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            ticketManager.initializeData();
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }

        boolean loggedIn = false;
        String username;
        String password;

        System.out.println(accountManager.findAccount(1234).getUserName());

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

        if(loggedInAccount instanceof AdminAccount) {
            while (true) {
                System.out.println("""
                        Welcome to RowdyDeskAdmin! please select an option
                        [1] view all active tickets
                        [2] view tickets assigned to you
                        [3] create new ticket
                        [4] edit your account info
                        [5] add new account
                        [6] quit""");

                userChoice = scnr.next();

                if (userChoice.equals("1")) {
                    //loop thru all tickets and print out
                } else if (userChoice.equals("2")) {
                    //get list of tickets assign to logged in user
                    // loop thru them and print out
                } else if (userChoice.equals("3")) {
                    // get valid information
                    // call create ticket
                } else if (userChoice.equals("4")) {
                    // get valid information
                    // swap account info
                } else if (userChoice.equals("5")) {
                    // get valid information
                    // add account
                } else if (userChoice.equals("6")) {
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
