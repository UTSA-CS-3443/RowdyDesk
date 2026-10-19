package programLogic.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class AccountManager {

    ArrayList<Account> allAcounts;

    public AccountManager() {
        allAcounts = new ArrayList<Account>();
    }

    // Returns account on successful login, throws exception otherwise
    public Account login(String userName, String password) throws Exception {
        for(Account acc : allAcounts) {
            if(acc.getUserName().equals(userName.toLowerCase())) {
                if(acc.getPassword().equals(password)) {
                    return acc;
                } else {
                    throw new Exception("Incorrect password.");
                }
            }
        }
        throw new Exception("User not found.");
    }

    private void saveAccounts(String path) {
        try {
            FileWriter fw = new FileWriter(path);
            for(Account acc : allAcounts) {
                fw.write(String.format("%s,%s,%s,%d,%s\n",
                        acc instanceof AdminAccount ? "admin" : "user",
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

    public void loadAccounts(String filename) throws Exception {
        try {
            Scanner f = new Scanner(new File(filename));
            while(f.hasNext()) {
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
        saveAccounts("programLogic/data/user_accounts.csv");
    }
    
}
