package programLogic.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class TicketManager {

    ArrayList<Ticket> activeTickets;
    ArrayList<Ticket> archivedTickets;

    AccountManager accountManager;

    String activeTicketsDataPath = "src/programLogic/data/ticketsData.csv";
    String archivedTicketsDataPath = "src/programLogic/data/archivedTicketsData.csv";

    private static TicketManager instance;

    public static TicketManager getInstance() throws Exception {
        if(instance == null) {
            instance = new TicketManager(AccountManager.getInstance());
            instance.initializeData();
        }
        return instance;
    }

    private int currentIDNumber;

    public ArrayList<Ticket> getActiveTickets() {
        return activeTickets;
    }

    public ArrayList<Ticket> getArchivedTickets() {
        return archivedTickets;
    }

    private TicketManager(AccountManager accountManager) {
        this.accountManager = accountManager;
        activeTickets = new ArrayList<Ticket>();
        archivedTickets = new ArrayList<Ticket>();
        currentIDNumber = 0;
    }

    public int getCurrentIDNumber() {
        return currentIDNumber;
    }

    public void setCurrentIDNumber(int currentIDNumber) {
        this.currentIDNumber = currentIDNumber;
    }


    public void addTicket(Ticket ticket) {
        activeTickets.add(ticket);
    }

    private void addArchivedTicket(Ticket ticket) { archivedTickets.add(ticket); }

    public Ticket createTicket(Account userName, String shortDescription, String longDescription) {
        Ticket newTicket = new Ticket(currentIDNumber, userName, null, shortDescription, longDescription, Status.UNASSIGNED,new Date());
        currentIDNumber += 1;

        addTicket(newTicket);
        saveDataToFile();
        return newTicket;
    }

    public Ticket findTicket(int id) {

        for(Ticket ticket: activeTickets) {
            if(ticket.getId() == id) {
                return ticket;
            }
        }

        return null;
    }

    public void assignTicket(Ticket ticket, AdminAccount assignTo) {
        if(ticket.assignedTo == assignTo) {
            return;
        }
        assignTo.addTicket(ticket);
        ticket.assignedTo = assignTo;
        ticket.setTicketStatus(Status.INPROGRESS);
        saveDataToFile();
    }

    public void addWorkNotesToTicket(Ticket ticket, String worknotes) {
        ticket.setLongDescription(ticket.getLongDescription() + "\n\n" + worknotes);
    }

    public void resolveTicket(Ticket ticket) {
        if(ticket.getTicketStatus() == Status.RESOLVED || ticket.getTicketStatus() == Status.DELETED) {
            return;
        }

        ticket.setTicketStatus(Status.RESOLVED);
        activeTickets.remove(ticket);
        archivedTickets.add(ticket);
        saveDataToFile();
    }
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
    public void deleteTicket(Ticket ticket) {
        if(ticket.getTicketStatus() == Status.RESOLVED || ticket.getTicketStatus() == Status.DELETED) {
            return;
        }
        ticket.setTicketStatus(Status.DELETED);
        activeTickets.remove(ticket);
        archivedTickets.add(ticket);
        saveDataToFile();
    }

    public void initializeData() throws IOException{
        try {
            Scanner f = new Scanner(new File(activeTicketsDataPath));
            while(f.hasNextLine()) {
                 Ticket newTicket = lineToTicket(f.nextLine());
                 addTicket(newTicket);
                 currentIDNumber = newTicket.getId() +1;
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
    }

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

    public String ticketToLine(Ticket ticket) {
        if(ticket.getAssignedTo() == null) {
            return String.format("%d,%d,%s,%s,%s,%d,%d\n",
                    ticket.getId(),
                    ticket.user.getId(),
                    "9999",
                    ticket.shortDescription,
                    ticket.longDescription,
                    ticket.getTicketStatus().ordinal(),
                    ticket.getTimeCreated().getTime());
        } else{
            return String.format("%d,%d,%d,%s,%s,%d,%d\n",
                ticket.getId(),
                ticket.user.getId(),
                ticket.assignedTo.getId(),
                ticket.shortDescription,
                ticket.longDescription,
                ticket.getTicketStatus().ordinal(),
                ticket.getTimeCreated().getTime());
        }
    }

    public void editTicket(Ticket newInfo) {
        Ticket ticketToUpdate = findTicket(newInfo.getId());
        activeTickets.set(activeTickets.indexOf(ticketToUpdate), newInfo);

        saveDataToFile();
    }

    public int getActiveTicketCount() {
        return activeTickets.size();
    }

    public ArrayList<Ticket> getAssignedTickets(AdminAccount admin) {
        //TODO Make this loop thru and add shi to the arraylist and then return it
        return new ArrayList<>();
    }

 }
