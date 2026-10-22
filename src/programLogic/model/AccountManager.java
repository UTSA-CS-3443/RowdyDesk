package programLogic.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class AccountManager {

    ArrayList<Account> allAcounts;
    String accountDataPath = "src/programLogic/data/user_accounts.csv";

    private AccountManager() {
        allAcounts = new ArrayList<Account>();
    }

    private static AccountManager instance;

    public ArrayList<Account> getAllAcounts() {
        return allAcounts;
    }

    public static AccountManager getInstance() throws Exception {
        if(instance == null) {
            instance = new AccountManager();
            instance.loadAccounts();
        }
        return instance;
    }

    // Returns account on successful login, throws exception otherwise
    public Account login(String userName, String password) throws Exception {
        for(Account acc : allAcounts) {
            if(acc.getUserName().equalsIgnoreCase(userName)) {
                if(acc.getPassword().equals(password)) {
                    return acc;
                } else {
                    throw new Exception("Incorrect password.");
                }
            }
        }
        throw new Exception("User not found.");
    }

    public void saveAccounts() {
        try(FileWriter fw = new FileWriter(accountDataPath);) {
            for(Account acc : allAcounts) {
                fw.write(String.format("%s,%s,%s,%d,%s\n",
                        acc.isAdminAccount() ? "admin" : "user",
                        acc.getUserName(),
                        acc.getDisplayName(),
                        acc.getId(),
                        acc.getPassword()
                ));
            }
        } catch(Exception e) {
            System.out.println("Error while saving user data.");
        }
    }

    public void loadAccounts() throws Exception {
        try {
            Scanner f = new Scanner(new File(accountDataPath));
            while(f.hasNextLine()) {
                String[] data = f.nextLine().split(",");
                if(data.length == 0) continue;
                if(data[0].equals("admin")) {
                    allAcounts.add(new AdminAccount(data[1], data[2], Integer.parseInt(data[3]), data[4]));
                } else {
                    allAcounts.add(new Account(data[1], data[2], Integer.parseInt(data[3]), data[4]));
                }
            }
        } catch(IOException e) {
            throw new IOException("Accounts file cannot be read.");
        } catch(Exception e) {
            throw new Error("Accounts file is formatted incorrectly");
        }
    }

    public void register(String userName, String displayName, String password) {
        allAcounts.add(new Account(userName, displayName, allAcounts.size(), password));
        saveAccounts();
    }

    public boolean changePassword(Account account,String oldPassword, String newPassword) {
        if(!account.getPassword().equals(oldPassword)) {
            return false;
        }
        account.setPassword(newPassword);
        saveAccounts();
        return true;
    }

    public void editAccount(Account newAccountInfo) {
        Account accountToUpdate = findAccount(newAccountInfo.getId());
        allAcounts.set(allAcounts.indexOf(accountToUpdate), newAccountInfo);

        saveAccounts();
    }

    public void deleteAccount(Account account) {
        allAcounts.remove(account);

        if(account instanceof AdminAccount) {
            for(Ticket ticket : ((AdminAccount) account).getAssignedTickets()) {
                ticket.setAssignedTo(null);
                ticket.setTicketStatus(Status.UNASSIGNED);
            }
        }
        saveAccounts();
    }

    public void elevateToAdmin(Account account) {
        if(account instanceof AdminAccount) {
            return;
        }
        int index = allAcounts.indexOf(account);
        allAcounts.remove(account);
        AdminAccount newAdmin = new AdminAccount(account.getUserName(),account.getDisplayName(), account.getId(),account.getPassword());
        allAcounts.add(index,newAdmin);

        saveAccounts();
    }

    public Account findAccount(int id) {

        for(Account acc : allAcounts) {
            if(acc.getId() == id) {
                return acc;
            }
        }
        return null;
    }
    public Account findAccount(String userName) {

        for(Account acc : allAcounts) {
            if(acc.getUserName().equals(userName)) {
                return acc;
            }
        }
        return null;
    }

    public AdminAccount findAdminAccount(int id) {

        for(Account acc : allAcounts) {
            if(acc.getId() == id && acc instanceof AdminAccount) {
                return (AdminAccount)acc;
            }
        }
        return null;
    }

    public AdminAccount findAdminAccount(String userName) {

        for(Account acc : allAcounts) {
            if(acc.getUserName().equals(userName) && acc instanceof AdminAccount) {
                return (AdminAccount)acc;
            }
        }
        return null;
    }
}
