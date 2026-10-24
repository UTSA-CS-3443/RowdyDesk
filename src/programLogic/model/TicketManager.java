package programLogic.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

/**
 * Manages active and archived support tickets and persists them to data files.
 */
public class TicketManager {

    ArrayList<Ticket> activeTickets;
    ArrayList<Ticket> archivedTickets;

    AccountManager accountManager;

    String activeTicketsDataPath = "src/programLogic/data/ticketsData.csv";
    String archivedTicketsDataPath = "src/programLogic/data/archivedTicketsData.csv";

    private static TicketManager instance;

    /**
     * Returns the singleton ticket manager, loading ticket data on first access.
     *
     * @return the shared ticket manager
     * @throws Exception if account or ticket data cannot be loaded
     */
    public static TicketManager getInstance() throws Exception {
        if(instance == null) {
            instance = new TicketManager(AccountManager.getInstance());
            instance.initializeData();
        }
        return instance;
    }

    private int currentIDNumber;

    /** @return all active tickets */
    public ArrayList<Ticket> getActiveTickets() {
        return activeTickets;
    }

    /** @return all archived tickets */
    public ArrayList<Ticket> getArchivedTickets() {
        return archivedTickets;
    }

    /**
     * Creates an empty ticket manager associated with an account manager.
     *
     * @param accountManager manager used to resolve ticket users and assignees
     */
    private TicketManager(AccountManager accountManager) {
        this.accountManager = accountManager;
        activeTickets = new ArrayList<Ticket>();
        archivedTickets = new ArrayList<Ticket>();
        currentIDNumber = 0;
    }

    /** @return the next identifier available for a new ticket */
    public int getCurrentIDNumber() {
        return currentIDNumber;
    }

    /** Sets the next identifier available for a new ticket. */
    public void setCurrentIDNumber(int currentIDNumber) {
        this.currentIDNumber = currentIDNumber;
    }

    /** Adds a ticket to the active ticket collection. */
    public void addTicket(Ticket ticket) {
        activeTickets.add(ticket);
    }

    /** Adds a ticket to the archived ticket collection. */
    private void addArchivedTicket(Ticket ticket) { archivedTickets.add(ticket); }

    /**
     * Creates, stores, and persists an unassigned ticket for a user.
     *
     * @return the newly created ticket
     */
    public Ticket createTicket(Account user, String shortDescription, String longDescription) {
        Ticket newTicket = new Ticket(currentIDNumber, user, null, shortDescription, longDescription, Status.UNASSIGNED,new Date());
        currentIDNumber += 1;

        addTicket(newTicket);
        user.addTicket(newTicket);
        saveDataToFile();
        return newTicket;
    }

    /**
     * Finds an active ticket by identifier.
     *
     * @return the matching ticket, or {@code null} when no match exists
     */
    public Ticket findTicket(int id) {

        for(Ticket ticket: activeTickets) {
            if(ticket.getId() == id) {
                return ticket;
            }
        }

        return null;
    }

    /** Assigns a ticket to an administrator and marks it in progress. */
    public void assignTicket(Ticket ticket, AdminAccount assignTo) {
        if(ticket.assignedTo == assignTo) {
            return;
        }
        assignTo.addTicket(ticket);
        ticket.assignedTo = assignTo;
        ticket.setTicketStatus(Status.INPROGRESS);
        saveDataToFile();
    }

    /** Appends work notes to a ticket's detailed description and persists the change. */
    public void addWorkNotesToTicket(Ticket ticket, String worknotes) {
        ticket.setLongDescription(ticket.getLongDescription() + "\n\n" + worknotes);
        saveDataToFile();
    }

    /** Resolves a ticket and moves it from the active collection to the archive. */
    public void resolveTicket(Ticket ticket) {
        if(ticket.getTicketStatus() == Status.RESOLVED || ticket.getTicketStatus() == Status.DELETED) {
            return;
        }

        ticket.setTicketStatus(Status.RESOLVED);
        activeTickets.remove(ticket);
        archivedTickets.add(ticket);
        saveDataToFile();
    }
    /** Reopens an archived ticket and restores its appropriate active status. */
    public void reopenTicket(Ticket ticket) {
        if(ticket.getTicketStatus() == Status.INPROGRESS || ticket.getTicketStatus() == Status.UNASSIGNED || ticket.getTicketStatus() == Status.PENDING) {
            return;
        }

        if(ticket.getAssignedTo() == null) {
            ticket.setTicketStatus(Status.UNASSIGNED);
        } else {
            ticket.setTicketStatus(Status.INPROGRESS);
        }

        archivedTickets.remove(ticket);
        activeTickets.add(ticket);

        saveDataToFile();
    }
    /** Marks a ticket deleted and moves it from the active collection to the archive. */
    public void deleteTicket(Ticket ticket) {
        if(ticket.getTicketStatus() == Status.RESOLVED || ticket.getTicketStatus() == Status.DELETED) {
            return;
        }
        ticket.setTicketStatus(Status.DELETED);
        activeTickets.remove(ticket);
        archivedTickets.add(ticket);
        saveDataToFile();
    }

    /**
     * Loads active and archived tickets from their data files.
     *
     * @throws IOException if either ticket file cannot be read
     */
    public void initializeData() throws IOException{
        try {
            Scanner f = new Scanner(new File(activeTicketsDataPath));
            while(f.hasNextLine()) {
                 Ticket newTicket = lineToTicket(f.nextLine());
                 addTicket(newTicket);

                 currentIDNumber = (newTicket.getId() >= currentIDNumber) ? newTicket.getId() + 1 : currentIDNumber;
                 if (newTicket.getAssignedTo() != null) {
                     newTicket.getAssignedTo().addTicket(newTicket);
                 }
                 newTicket.user.addTicket(newTicket);
            }
        } catch(IOException e) {
            throw new IOException("Active Tickets file cannot be read.");
        } catch(Exception e) {
            throw new Error("Active Tickets file is formatted incorrectly");
        }

        try {
            Scanner f = new Scanner(new File(archivedTicketsDataPath));
            while(f.hasNextLine()) {
                Ticket newTicket = lineToTicket(f.nextLine());
                addArchivedTicket(newTicket);
                currentIDNumber = (newTicket.getId() >= currentIDNumber) ? newTicket.getId() + 1 : currentIDNumber;
                if (newTicket.getAssignedTo() != null) {
                    newTicket.getAssignedTo().addTicket(newTicket);
                }
            }
        } catch(IOException e) {
            throw new IOException("Archived tickets file cannot be read.");
        } catch(Exception e) {
            throw new Error("Archived tickets file is formatted incorrectly");
        }
    }

    /** Persists active and archived tickets to their respective data files. */
    public void saveDataToFile() {
        try(FileWriter fw = new FileWriter(activeTicketsDataPath);) {
            for(Ticket ticket : activeTickets) {
                fw.write(ticketToLine(ticket));
            }
        } catch(Exception e) {
            System.out.println("Error while trying to save Active Ticket data to file");
            System.out.println(e.getMessage());
        }

        try(FileWriter fw = new FileWriter(archivedTicketsDataPath);) {
            for(Ticket ticket : archivedTickets) {
                fw.write(ticketToLine(ticket));
            }
        } catch(Exception e) {
            System.out.println("Error while trying to save Archived Ticket data to file");
        }
    }

    /**
     * Parses a persisted CSV record into a ticket.
     *
     * @param line serialized ticket data
     * @return the ticket represented by the record
     */
    public Ticket lineToTicket(String line) {
        String[] splitLine = line.split("(?<!\\\\),");
        int ticketId = Integer.parseInt(splitLine[0]);
        Account user = accountManager.findAccount(Integer.parseInt(splitLine[1])); // make this the Account finder based off the name
        AdminAccount assignedTo = accountManager.findAdminAccount(Integer.parseInt(splitLine[2])) ; // make 2nd null the Admin account based on Name
        String shortDesc = splitLine[3];
        String longDesc = splitLine[4].replaceAll("\\\\,", ",");
        Status ticketStatus = Status.parseInt(splitLine[5]);
        Date ticketCreated = new Date(Long.parseLong(splitLine[6]));

        return new Ticket(ticketId, user, assignedTo, shortDesc, longDesc,ticketStatus, ticketCreated);
    }

    /**
     * Serializes a ticket to the CSV format used by the ticket data files.
     *
     * @return the serialized ticket record
     */
    public String ticketToLine(Ticket ticket) {
        if(ticket.getAssignedTo() == null) {
            return String.format("%d,%d,%s,%s,%s,%d,%d\n",
                    ticket.getId(),
                    ticket.user.getId(),
                    "0000",
                    ticket.shortDescription,
                    ticket.longDescription.replace(",", "\\,"),
                    ticket.getTicketStatus().ordinal(),
                    ticket.getTimeCreated().getTime());
        } else{
            return String.format("%d,%d,%d,%s,%s,%d,%d\n",
                ticket.getId(),
                ticket.user.getId(),
                ticket.assignedTo.getId(),
                ticket.shortDescription,
                ticket.longDescription.replace(",", "\\,"),
                ticket.getTicketStatus().ordinal(),
                ticket.getTimeCreated().getTime());
        }
    }

    /** Replaces an active ticket with updated information and persists the change. */
    public void editTicket(Ticket newInfo) {
        Ticket ticketToUpdate = findTicket(newInfo.getId());
        activeTickets.set(activeTickets.indexOf(ticketToUpdate), newInfo);

        saveDataToFile();
    }

    /** @return the number of active tickets */
    public int getActiveTicketCount() {
        return activeTickets.size();
    }



 }
