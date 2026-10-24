package programLogic.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Manages account records, authentication, and persistence to the account data file.
 */
public class AccountManager {

    ArrayList<Account> allAcounts;
    String accountDataPath = "src/programLogic/data/user_accounts.csv";
    int currentIDNumber;

    private AccountManager() {
        allAcounts = new ArrayList<Account>();
    }

    private static AccountManager instance;

    /** @return all accounts currently managed by this instance */
    public ArrayList<Account> getAllAcounts() {
        return allAcounts;
    }

    /**
     * Returns the singleton account manager, loading account data on first access.
     *
     * @return the shared account manager
     * @throws Exception if account data cannot be loaded
     */
    public static AccountManager getInstance() throws Exception {
        if(instance == null) {
            instance = new AccountManager();
            instance.loadAccounts();
        }
        return instance;
    }

    /**
     * Authenticates an account using its username and password.
     *
     * @param userName username to authenticate
     * @param password password to verify
     * @return the matching account
     * @throws Exception if the user is not found or the password is incorrect
     */
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

    /** Saves all managed accounts to the account data file. */
    public void saveAccounts() {
        try(FileWriter fw = new FileWriter(accountDataPath);) {
            for(Account acc : allAcounts) {
                fw.write(String.format("%d,%s,%s,%s,%s\n",
                        acc.getId(),
                        acc.isAdminAccount() ? "admin" : "user",
                        acc.getDisplayName(),
                        acc.getUserName(),
                        acc.getPassword()
                ));
            }
        } catch(Exception e) {
            System.out.println("Error while saving user data.");
        }
    }

    /**
     * Loads accounts from the account data file.
     *
     * @throws Exception if the file cannot be read or is malformed
     */
    public void loadAccounts() throws Exception {
        try {
            Scanner f = new Scanner(new File(accountDataPath));
            while(f.hasNextLine()) {
                String[] data = f.nextLine().split(",");
                if(data.length == 0) continue;
                if(data[1].equals("admin")) {
                    allAcounts.add(new AdminAccount(Integer.parseInt(data[0]), data[2], data[3], data[4]));
                } else {
                    allAcounts.add(new Account(Integer.parseInt(data[0]), data[2], data[3], data[4]));
                }
                currentIDNumber = (Integer.parseInt(data[0]) >= currentIDNumber) ? Integer.parseInt(data[0]) + 1 : currentIDNumber;
            }
        } catch(IOException e) {
            throw new IOException("Accounts file cannot be read.");
        } catch(Exception e) {
            throw new Error("Accounts file is formatted incorrectly");
        }
    }

    /**
     * Registers and persists a new standard account.
     *
     * @return the newly created account
     */
    public Account register(String displayName, String userName, String password) {
        Account newAccount = new Account(currentIDNumber, displayName, userName, password);
        allAcounts.add(newAccount);
        saveAccounts();
        return newAccount;
    }

    /**
     * Changes an account password after verifying the old password.
     *
     * @return {@code true} if the password was changed
     */
    public boolean changePassword(Account account,String oldPassword, String newPassword) {
        if(!account.getPassword().equals(oldPassword)) {
            return false;
        }
        account.setPassword(newPassword);
        saveAccounts();
        return true;
    }

    /** Replaces a stored account with updated account information and persists it. */
    public void editAccount(Account newAccountInfo) {
        Account accountToUpdate = findAccount(newAccountInfo.getId());
        allAcounts.set(allAcounts.indexOf(accountToUpdate), newAccountInfo);

        saveAccounts();
    }

    /** Deletes an account and unassigns any tickets assigned to an administrator. */
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

    /** Elevates a standard account to an administrator account. */
    public void elevateToAdmin(Account account) {
        if(account instanceof AdminAccount) {
            return;
        }
        int index = allAcounts.indexOf(account);
        allAcounts.remove(account);
        AdminAccount newAdmin = new AdminAccount(account.getId(), account.getDisplayName(), account.getUserName(), account.getPassword());
        allAcounts.add(index,newAdmin);

        saveAccounts();
    }

    /**
     * Finds an account by its numeric identifier.
     *
     * @return the matching account, or {@code null} when no match exists
     */
    public Account findAccount(int id) {

        for(Account acc : allAcounts) {
            if(acc.getId() == id) {
                return acc;
            }
        }
        return null;
    }
    /**
     * Finds an account by its username.
     *
     * @return the matching account, or {@code null} when no match exists
     */
    public Account findAccount(String userName) {

        for(Account acc : allAcounts) {
            if(acc.getUserName().equals(userName)) {
                return acc;
            }
        }
        return null;
    }

    /**
     * Finds an administrator account by numeric identifier.
     *
     * @return the matching administrator, or {@code null} when no match exists
     */
    public AdminAccount findAdminAccount(int id) {

        for(Account acc : allAcounts) {
            if(acc.getId() == id && acc instanceof AdminAccount) {
                return (AdminAccount)acc;
            }
        }
        return null;
    }

    /**
     * Finds an administrator account by username.
     *
     * @return the matching administrator, or {@code null} when no match exists
     */
    public AdminAccount findAdminAccount(String userName) {

        for(Account acc : allAcounts) {
            if(acc.getUserName().equals(userName) && acc instanceof AdminAccount) {
                return (AdminAccount)acc;
            }
        }
        return null;
    }
}
