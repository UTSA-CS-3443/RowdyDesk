package programLogic.controllerCode;

import programLogic.model.*;

public class AdminViewAllTicketsController {


    public static String viewAllTickets(TicketManager ticketManager) {
        String outputString = String.format("|%-10s |%-25s |%-10s |%-15s |%-15s\n", "ID Num", "Short Description", "Status", "User", "Assigned To");
        outputString += "_________________________________________________________________________________\n";
        for(Ticket ticket: ticketManager.getActiveTickets()) {
            outputString += String.format("|%-10s |%-25s |%-10s |%-15s|",
                    ticket.getId(),
                    ticket.getShortDescription(),
                    ticket.getTicketStatus(),
                    ticket.getUser().getUserName());
            if(ticket.getAssignedTo() != null) {
                outputString += String.format("%-15s",ticket.getAssignedTo().getUserName());
            }
            outputString += "\n";
        }

        return outputString;
    }

    public static String viewAssignedTickets(AdminAccount adminAcc) {
        String outputString = String.format("|%-10s |%-25s |%-10s |%-15s |%-15s\n", "ID Num", "Short Description", "Status", "User", "Assigned To");
        outputString += "_________________________________________________________________________________\n";
        for(Ticket ticket: adminAcc.getAssignedTickets()) {
            outputString += String.format("|%-10s |%-25s |%-10s |%-15s|",
                    ticket.getId(),
                    ticket.getShortDescription(),
                    ticket.getTicketStatus(),
                    ticket.getUser().getUserName());
            if(ticket.getAssignedTo() != null) {
                outputString += String.format("%-15s",ticket.getAssignedTo().getUserName());
            }
            outputString += "\n";
        }

        return outputString;
    }

    public static void editTicket(TicketManager ticketManager, AccountManager accountManager) {
        System.out.println("Please enter the id of the ticket you'd like to edit");
        Ticket foundTicket;
        while (true) {
            foundTicket = ticketManager.findTicket(InputValidator.readIntNumber("Please enter a valid number"));

            if(foundTicket != null) {
                System.out.println("Found ticket!");
                break;
            }
            System.out.println("Ticket not found, please try again");
        }



        int response;

        while(true) {
            System.out.println("What would you like to do with ticket: "+ foundTicket.getId());
            System.out.println("""
                        [1] Edit Short Description and User
                        [2] Add Work Notes
                        [3] Reassign ticket
                        [4] Resolve Ticket
                        [5] Re-open Ticket
                        [6] delete Ticket
                        [7] Do nothing and quit""");

            response = InputValidator.readIntNumberBetween(1,7,"Please enter a number between 1 and 7");
            if(response == 1) {
                System.out.println("Please enter the new Short Description");
                String shortDesc = InputValidator.readLine();

                System.out.println("Please enter the new User, or type leave blank to keep it the same");
                Account user;
                while(true) {
                    String input = InputValidator.readString();
                    user = accountManager.findAccount(input);


                    if(user != null) {
                        break;
                    } else if (input.isBlank()) {
                        break;
                    }
                    System.out.println("Error, user not found. Leave blank to keep the same user");
                }

                foundTicket.setShortDescription(shortDesc);
                if(user != null) {
                    foundTicket.setUser(user);
                }
                ticketManager.editTicket(foundTicket);
                System.out.println("Ticket successfully updated!");

            } else if (response == 2) {
                System.out.println("Add any work notes you like, or leave blank to cancel:");
                String workNotes = InputValidator.readLine();
                if(workNotes.isBlank()) {
                    continue;
                }
                ticketManager.addWorkNotesToTicket(foundTicket,workNotes);
                System.out.println("Work notes successfully added");
            } else if (response == 3) {
                System.out.println("Please enter the new admin who is assigned to this ticket");
                AdminAccount admin;
                while(true) {
                    String input = InputValidator.readString();
                    admin = accountManager.findAdminAccount(input);


                    if(admin != null) {
                        break;
                    } else if(input.isBlank()) {
                        break;
                    }
                    System.out.println("Error, admin account not found. Leave blank to cancel");
                }
                if(admin == null) {
                    System.out.println("Cancelling...");
                    continue;
                }
                ticketManager.assignTicket(foundTicket, admin);
                System.out.println("Ticket Successfully assigned to "+ admin.getUserName());
            } else if (response == 4) {
                ticketManager.resolveTicket(foundTicket);
                System.out.println("Ticket is Resolved!");
            } else if (response == 5) {
                ticketManager.reopenTicket(foundTicket);
            } else if (response == 6) {
                ticketManager.deleteTicket(foundTicket);
                System.out.println("Ticket successfully deleted!");
                break;
            } else if (response == 7) {
                System.out.println("Going back to main screen...\n");
            }
        }
    }
}
