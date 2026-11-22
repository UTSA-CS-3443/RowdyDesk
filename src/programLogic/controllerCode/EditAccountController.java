package programLogic.controllerCode;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import programLogic.model.Account;
import programLogic.model.AccountManager;

import javax.security.auth.login.LoginException;


/**
 * Handles console login and account-creation workflows.
 */
public class EditAccountController {
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
    public void initialize() {
        Account acc = AccountManager.getInstance().getLoggedIn();
        usernameField.setText(acc.getUserName());
        emailField.setText(acc.getEmail());
        phoneField.setText(acc.getPhone());
        displayNameField.setText(acc.getDisplayName());
    }

    @FXML
    protected void onCancelButtonClicked(Event event) {
        SceneSwitcher.switchScene(event, "/resources/user-dashboard.fxml");
    }

    @FXML
    protected void onDoneButtonClicked(Event event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String displayName = displayNameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();

        try {
            Account loggedIn = AccountManager.getInstance().getLoggedIn();
            AccountManager.getInstance().login(loggedIn.getUserName(), password);

            loggedIn.setUserName(username);
            loggedIn.setDisplayName(displayName);
            loggedIn.setEmail(email);
            loggedIn.setPhone(phone);

            AccountManager.getInstance().editAccount(loggedIn);
            SceneSwitcher.switchScene(event, "/resources/user-dashboard.fxml");
        } catch(LoginException e) {
            errorLabel.setText(e.getMessage());
        }
    }

    @FXML
    protected void enterKeyPressed(KeyEvent event) {
        if( event.getCode() == KeyCode.ENTER) {
            onDoneButtonClicked(event);
        }
    }
}
