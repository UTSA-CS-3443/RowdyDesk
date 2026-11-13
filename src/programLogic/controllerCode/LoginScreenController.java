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
import programLogic.model.AdminAccount;

import javax.security.auth.login.LoginException;
import java.io.IOException;


/**
 * Handles console login and account-creation workflows.
 */
public class LoginScreenController {
    @FXML
    protected TextField usernameField;

    @FXML
    protected PasswordField passwordField;

    @FXML
    protected TextField emailField;

    @FXML
    protected TextField phoneField;

    @FXML
    protected TextField displayNameField;

    @FXML
    protected Label errorLabel;

    @FXML
    protected Button createAccountButton;

    @FXML
    protected Button cancelCreateAccountButton;

    @FXML
    protected Button loginButton;

    @FXML
    public void initialize() {
        try {
            AccountManager.getInstance();
        } catch(Exception e) {
            errorLabel.setText(e.getMessage());
        }
        closeAccountCreation();
    }

    @FXML
    protected void onCreateAccountButtonClicked() {
        if(loginButton.isVisible()) {
            openAccountCreation();
            errorLabel.setText("Please fill out the fields.");
            return;
        }

        String username = usernameField.getText();
        String password = passwordField.getText();
        String displayName = displayNameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();

        if(!InputValidator.isValidUsername(username)) {
            errorLabel.setText("Invalid username. Usernames should be between 3 and 16 characters and use only uppercase and lowercase letters, numbers, and underscores.");
            return;
        }
        if(!InputValidator.isValidPassword(password)) {
            errorLabel.setText("Invalid password. Passwords should be between 8 and 64 characters and not use commas.");
            return;
        }

        AccountManager.getInstance().register(username, password, displayName, email, phone, "fakefilepath");
        errorLabel.setText("Account created! You can now log in.");
        closeAccountCreation();
    }

    @FXML
    protected void onCancelCreateAccountButtonClicked() {
        closeAccountCreation();
    }

    @FXML
    protected void onLoginButtonClicked(MouseEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            AccountManager.getInstance().login(username, password);
            if(AccountManager.getInstance().getLoggedIn() instanceof AdminAccount) {
                AdminDashboardController controller = (AdminDashboardController)SceneSwitcher.switchScene(event, "/resources/admin-dashboard.fxml");
            } else {
                UserDashboardController controller = (UserDashboardController)SceneSwitcher.switchScene(event,"/resources/user-dashboard.fxml");
            }
        } catch(Exception e) {
            System.out.println(e.getMessage());
            errorLabel.setText(e.getMessage());
        }
    }

    private void openAccountCreation() {
        displayNameField.setVisible(true);
        displayNameField.setManaged(true);
        emailField.setVisible(true);
        emailField.setManaged(true);
        phoneField.setVisible(true);
        phoneField.setManaged(true);

        cancelCreateAccountButton.setVisible(true);
        cancelCreateAccountButton.setManaged(true);
        loginButton.setVisible(false);
        loginButton.setManaged(false);
    }

    private void closeAccountCreation() {
        displayNameField.setVisible(false);
        displayNameField.setManaged(false);
        displayNameField.clear();
        emailField.setVisible(false);
        emailField.setManaged(false);
        emailField.clear();
        phoneField.setVisible(false);
        phoneField.setManaged(false);
        phoneField.clear();

        cancelCreateAccountButton.setVisible(false);
        cancelCreateAccountButton.setManaged(false);
        loginButton.setVisible(true);
        loginButton.setManaged(true);
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

                accountManager.login(username,password);

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

        // OLD
        Account newAccount = accountManager.register(userName, password, displayName,"","","");
        System.out.println("Account successfully created!");
        return newAccount;
    }
}
