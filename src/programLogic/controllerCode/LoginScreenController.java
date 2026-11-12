package programLogic.controllerCode;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import programLogic.model.Account;
import programLogic.model.AccountManager;

import javax.security.auth.login.LoginException;


/**
 * Handles console login and account-creation workflows.
 */
public class LoginScreenController {
    protected AccountManager accountManager = new AccountManager();

    @FXML
    protected TextField usernameField;

    @FXML
    protected PasswordField passwordField;

    @FXML
    protected Label errorLabel;

    @FXML
    protected Button createAccountButton;

    @FXML
    protected Button loginButton;

    @FXML
    public void initialize() {
        try {
            accountManager.loadAccounts();
        } catch(Exception e) {
            errorLabel.setText(e.getMessage());
        }
    }

    @FXML
    protected void onCreateAccountButtonClicked() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if(!InputValidator.isValidUsername(username)) {
            errorLabel.setText("Invalid username. Usernames should be between 3 and 16 characters and use only uppercase and lowercase letters, numbers, and underscores.");
            return;
        }
        if(!InputValidator.isValidPassword(password)) {
            errorLabel.setText("Invalid password. Passwords should be between 8 and 64 characters and not use commas.");
            return;
        }

        errorLabel.setText("todo make account boy");
    }

    @FXML
    protected void onLoginButtonClicked(MouseEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            Account loginAccount = accountManager.login(username, password);
            SceneSwitcher.switchScene(event, "/resources/user-dashboard.fxml");
        } catch(Exception e) {
            errorLabel.setText(e.getMessage());
        }
    }

    // CLI login
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
