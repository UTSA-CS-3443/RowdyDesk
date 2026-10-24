package programLogic.controllerCode;

import programLogic.model.Account;
import programLogic.model.AccountManager;

/**
 * Handles console login and account-creation workflows.
 */
public class LoginScreenController {

    public static Account logIn(AccountManager accountManager) {

        Account loggedInAccount = null;
        String username;
        String password;

        boolean loggedIn = false;
        while(!loggedIn) {
            try {
                System.out.println("Please Enter your Username:");
                username = InputValidator.readString();
                System.out.println("Please Enter your Password:");
                password = InputValidator.readString();

                loggedInAccount = accountManager.login(username,password);

                System.out.println("Successfully logged in! Hello "+username+"!");
                loggedIn = true;

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }

        return loggedInAccount;
    }

    public static Account createAccount(AccountManager accountManager) {
        System.out.println("Please create your Username");
        String userName = InputValidator.readString();

        System.out.println("Please create your Display Name");
        String displayName = InputValidator.readString();

        System.out.println("Please create your Password");
        String password = InputValidator.readPassword();

        Account newAccount = accountManager.register(userName, displayName, password);
        System.out.println("Account successfully created!");
        return newAccount;
    }
}
