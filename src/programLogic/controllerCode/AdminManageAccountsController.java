package programLogic.controllerCode;

import programLogic.model.AccountManager;
import programLogic.model.Account;
import programLogic.model.AdminAccount;

public class AdminManageAccountsController {

    // view all accounts
    public static String viewAllAccounts(AccountManager accountManager) {
        String outputString = String.format("|%-10s |%-5s |%-25s |%-25s\n", "ID Num", "Priv", "Display Name", "User Name");
        outputString += "_________________________________________________________________________________\n";
        for(Account account : accountManager.getAllAcounts()) {
            outputString += String.format("|%-10s |%-5s |%-25s |%-25s|\n",
                    account.getId(),
                    account.isAdminAccount()? "admin" : "user",
                    account.getDisplayName(),
                    account.getUserName()
            );

        }

        return outputString;
    }

    // create new account
    public static void createAccount(AccountManager accountManager) {
        System.out.println("Please enter the Username");
        String userName = InputValidator.readString();

        System.out.println("Please enter Display Name");
        String displayName = InputValidator.readString();

        System.out.println("Please enter a Password");
        String password = InputValidator.readPassword();

        accountManager.register(userName, displayName, password);
    }

    // find specific account
    public static Account findAccount(AccountManager accountManager) {
        System.out.println("Please enter the username of the account");
        Account foundAccount;
        while(true) {
            String input = InputValidator.readString();
            foundAccount = accountManager.findAdminAccount(input);


            if(foundAccount != null) {
                break;
            } else if (input.isBlank()) {
                return null;
            }
            System.out.println("Error, admin account not found. Leave field blank to go back");
        }

        return foundAccount;
    }

    // edit specific account
    public static void editAccount(AccountManager accountManager) {
        System.out.println("Please enter the id of the user you'd like to edit");
        Account foundAccount;
        while (true) {

            foundAccount = accountManager.findAccount(InputValidator.readIntNumber("Please enter a valid number"));

            if(foundAccount != null) {
                if (foundAccount.getId() == 0) {
                    System.out.println("cancelling...");
                    return;
                }

                System.out.println("Found Account!");
                break;
            }
            System.out.println("Account not found, please try again or enter 0 to cancel");
        }



        int response;

        while(true) {
            System.out.println("What would you like to do with user " + foundAccount.getUserName());
            System.out.println("""
                        [1] Edit Username
                        [2] Edit Display Name
                        [3] Elevate to Admin
                        [4] Delete Account
                        [5] Do nothing and quit""");

            response = InputValidator.readIntNumberBetween(1,5, "Please enter a valid number between 1 and 5");
            if (response == 1) {
                System.out.println("Please enter your new Username:");
                String userName = InputValidator.readString();
                foundAccount.setUserName(userName);
                System.out.println("Updated username to " + userName);
            } else if (response == 2) {
                System.out.println("Please enter your new Display Name:");
                String displayName = InputValidator.readString();
                foundAccount.setDisplayName(displayName);
                System.out.println("Updated display Name to " + displayName);
            } else if (response == 3) {
                accountManager.elevateToAdmin(foundAccount);
                System.out.println("Successfully made user into an Admin");
            } else if (response == 4) {
                System.out.println("Are you sure your want to delete " + foundAccount.getUserName() + "?");
                boolean confirm = InputValidator.readBooleanValue("Enter yes or no");
                if(confirm) {
                    accountManager.deleteAccount(foundAccount);
                    System.out.println("Account Deleted");
                } else {
                    System.out.println("Account NOT deleted");
                }
            } else if (response == 5) {
                break;
            }
        }
    }

    public static boolean editOwnAccount(AdminAccount account, AccountManager accountManager) {

        int response;
        while (true) {
            System.out.println("What would you like to do with your account, " + account.getUserName() + "?");
            System.out.println("""
                [1] Edit Username
                [2] Edit Display Name
                [3] Change Password
                [4] Delete Account
                [5] Do nothing and quit""");
            response = InputValidator.readIntNumberBetween(1, 5, "Please enter a valid number between 1 and 5");
            if (response == 1) {
                System.out.println("Please enter your new username");
                String userName = InputValidator.readString();
                account.setUserName(userName);
                System.out.println("Updated username to " + userName);
            } else if (response == 2) {
                System.out.println("Please enter your new display name");
                String displayName = InputValidator.readString();
                account.setDisplayName(displayName);
                System.out.println("Updated display Name to " + displayName);
            } else if (response == 3) {
                System.out.println("Please enter your old password, followed by your new password");
                String oldPass = InputValidator.readString();
                String newPass = InputValidator.readString();
                if (accountManager.changePassword(account, oldPass, newPass)) {
                    System.out.println("password change successful!");
                } else {
                    System.out.println("old password is incorrect");
                }
            } else if (response == 4) {
                System.out.println("Are you sure your want to delete your own account, " + account.getUserName() + "?");
                boolean confirm = InputValidator.readBooleanValue("Enter yes or no");
                if (confirm) {
                    accountManager.deleteAccount(account);
                    System.out.println("Account Deleted");
                    return true;
                } else {
                    System.out.println("Account NOT deleted");
                }
            } else if (response == 5) {
                return false;
            }

        }
    }

    // delete account
    public static void deleteAccount(AccountManager accountManager, Account account) {
        accountManager.deleteAccount(account);
        System.out.println("Account successfully deleted!");
    }

}
