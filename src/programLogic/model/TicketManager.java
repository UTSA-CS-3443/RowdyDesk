package programLogic.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

/**
 * Manages active and archived support tickets and persists them to data files.
 */
public class TicketManager {

    public static final int FIRST_TICKET_ID = 1;

    ArrayList<Ticket> activeTickets;
    ArrayList<Ticket> archivedTickets;

    ArrayList<Ticket> draftTickets;

    AccountManager accountManager;

    String activeTicketsDataPath = "src/programLogic/data/ticketsData.csv";
    String archivedTicketsDataPath = "src/programLogic/data/archivedTicketsData.csv";
    String draftTicketsDataPath = "src/programLogic/data/draftTicketsData.csv";

    private static TicketManager instance;

    /**
     * Returns the singleton ticket manager, loading ticket data on first access.
     *
     * @return the shared ticket manager
     */
    public static TicketManager getInstance() {
        try {
            if (instance == null) {
                instance = new TicketManager(AccountManager.getInstance());
                instance.initializeData();
            }
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return instance;
    }

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
        draftTickets = new ArrayList<Ticket>();
    }

    private int nextTicketId() {
        HashSet<Integer> inUse = new HashSet<>();
        for(Ticket t : activeTickets)   inUse.add(t.getId());
        for(Ticket t : archivedTickets) inUse.add(t.getId());
        for(Ticket t : draftTickets)    inUse.add(t.getId());

        int id = FIRST_TICKET_ID;
        while(inUse.contains(id)) {
            id++;
        }
        return id;
    }

    /** @return the next identifier available for a new ticket */
    public int getCurrentIDNumber() {
        return nextTicketId();
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
        Ticket newTicket = new Ticket(nextTicketId(), user, null, shortDescription, longDescription, Status.UNASSIGNED,new Date());

        addTicket(newTicket);
        user.addTicket(newTicket);
        saveDataToFile();
        return newTicket;
    }


    public Ticket saveDraft(Ticket draft, Account author, Account openedFor, AdminAccount assignedTo,
                            String shortDescription, String longDescription) {
        if(draft == null) {
            draft = new Ticket(nextTicketId(), openedFor, assignedTo, shortDescription, longDescription, Status.DRAFT, new Date());
            draft.setAuthor(author);
            draftTickets.add(draft);
        } else {
            draft.setUser(openedFor);
            draft.setAssignedTo(assignedTo);
            draft.setShortDescription(shortDescription);
            draft.setLongDescription(longDescription);
        }
        saveDataToFile();
        return draft;
    }

    public Ticket submitDraft(Ticket draft, Account openedFor, AdminAccount assignTo,
                              String shortDescription, String longDescription) {
        draftTickets.remove(draft);

        draft.setUser(openedFor);
        draft.setAssignedTo(null);
        draft.setShortDescription(shortDescription);
        draft.setLongDescription(longDescription);
        draft.setTicketStatus(Status.UNASSIGNED);
        draft.setAuthor(null);

        addTicket(draft);
        openedFor.addTicket(draft);
        if(assignTo != null) {
            assignTicket(draft, assignTo);
        }
        saveDataToFile();
        return draft;
    }

    public void discardDraft(Ticket draft) {
        draftTickets.remove(draft);
        saveDataToFile();
    }

    public ArrayList<Ticket> getDraftsFor(Account author) {
        ArrayList<Ticket> mine = new ArrayList<>();
        for(Ticket draft : draftTickets) {
            if(draft.getAuthor() != null && draft.getAuthor().getId() == author.getId()) {
                mine.add(draft);
            }
        }
        return mine;
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
        if(ticket.assignedTo != null) {
            ticket.assignedTo.removeTicket(ticket);
        }
        ticket.assignedTo = assignTo;
        if(assignTo == null) {
            ticket.setTicketStatus(Status.UNASSIGNED);
        } else {
            assignTo.addTicket(ticket);
            ticket.setTicketStatus(Status.INPROGRESS);
        }
        saveDataToFile();
    }

    public void relinkAccount(Account oldAccount, Account newAccount) {
        for(List<Ticket> list : List.of(activeTickets, archivedTickets, draftTickets)) {
            for(Ticket t : list) {
                if(t.user == oldAccount) t.user = newAccount;
                if(t.getAuthor() == oldAccount) t.setAuthor(newAccount);
                if(t.assignedTo == oldAccount) t.assignedTo = (newAccount instanceof AdminAccount) ? (AdminAccount) newAccount : null;
            }
        }
        saveDataToFile();
    }

    public int countTicketsFor(Account account) {
        int count = 0;
        for(List<Ticket> list : List.of(activeTickets, archivedTickets, draftTickets)) {
            for(Ticket t : list) {
                boolean attached = (t.user != null && t.user.getId() == account.getId())
                        || (t.assignedTo != null && t.assignedTo.getId() == account.getId())
                        || (t.getAuthor() != null && t.getAuthor().getId() == account.getId());
                if(attached) count++;
            }
        }
        return count;
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
                if (newTicket.getAssignedTo() != null) {
                    newTicket.getAssignedTo().addTicket(newTicket);
                }
            }
        } catch(IOException e) {
            throw new IOException("Archived tickets file cannot be read.");
        } catch(Exception e) {
            throw new Error("Archived tickets file is formatted incorrectly");
        }

        loadDrafts();
    }

    private void loadDrafts() {
        File draftFile = new File(draftTicketsDataPath);
        if(!draftFile.exists()) {
            return;
        }
        try(Scanner f = new Scanner(draftFile)) {
            while(f.hasNextLine()) {
                String line = f.nextLine();
                if(line.isBlank()) continue;
                try {
                    Ticket draft = lineToDraft(line);
                    if(draft.getAuthor() == null) {
                        System.out.println("Skipping draft #" + draft.getId() + ": its author no longer exists");
                        continue;
                    }
                    draftTickets.add(draft);
                } catch(Exception e) {
                    System.out.println("Skipping unreadable draft line: " + e.getMessage());
                }
            }
        } catch(IOException e) {
            System.out.println("Draft tickets file cannot be read.");
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

        try(FileWriter fw = new FileWriter(draftTicketsDataPath);) {
            for(Ticket draft : draftTickets) {
                fw.write(draftToLine(draft));
            }
        } catch(Exception e) {
            System.out.println("Error while trying to save Draft Ticket data to file");
        }
    }

    private String draftToLine(Ticket draft) {
        return draft.getAuthor().getId() + "," + ticketToLine(draft);
    }

    private Ticket lineToDraft(String line) {
        int firstComma = line.indexOf(',');
        Account author = accountManager.findAccount(Integer.parseInt(line.substring(0, firstComma)));

        Ticket draft = lineToTicket(line.substring(firstComma + 1));
        draft.setAuthor(author);
        return draft;
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
        String shortDesc = unescape(splitLine[3]);
        String longDesc = unescape(splitLine[4]);
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
        int userId = (ticket.user == null) ? -1 : ticket.user.getId();
        String assignedId = (ticket.getAssignedTo() == null) ? "0000" : String.valueOf(ticket.assignedTo.getId());

        return String.format("%d,%d,%s,%s,%s,%d,%d\n",
                ticket.getId(),
                userId,
                assignedId,
                escape(ticket.shortDescription),
                escape(ticket.longDescription),
                ticket.getTicketStatus().ordinal(),
                ticket.getTimeCreated().getTime());
    }

    private String escape(String text) {
        return text.replace(",", "\\,").replace("\r", "").replace("\n", "\\n");
    }

    private String unescape(String text) {
        return text.replace("\\,", ",").replace("\\n", "\n");
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
