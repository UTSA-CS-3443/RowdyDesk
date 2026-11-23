package programLogic.controllerCode;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import programLogic.model.Account;
import programLogic.model.AccountManager;
import programLogic.model.AdminAccount;
import programLogic.model.Status;
import programLogic.model.Ticket;
import programLogic.model.TicketManager;

import java.util.ArrayList;
import java.util.List;

public class AdminModifyUserController {

    private static final String DASHBOARD = "/resources/admin-dash.fxml";

    private Account account;

    @FXML
    protected Label titleLabel;

    @FXML
    protected Label roleLabel;

    @FXML
    protected Label ticketsLabel;

    @FXML
    protected TextField usernameField;

    @FXML
    protected TextField displayNameField;

    @FXML
    protected TextField emailField;

    @FXML
    protected TextField phoneField;

    @FXML
    protected PasswordField newPasswordField;

    @FXML
    protected Button promoteButton;

    @FXML
    protected Button demoteButton;

    @FXML
    protected Label errorDisplay;

    public void loadAccount(Account picked) {
        account = picked;
        titleLabel.setText("Modify User: " + picked.getUserName());
        usernameField.setText(picked.getUserName());
        displayNameField.setText(picked.getDisplayName());
        emailField.setText(picked.getEmail());
        phoneField.setText(picked.getPhone());
        newPasswordField.setText("");
        errorDisplay.setText("");

        roleLabel.setText(picked.isAdminAccount() ? "Role: Admin" : "Role: User");
        promoteButton.setVisible(!picked.isAdminAccount());
        promoteButton.setManaged(!picked.isAdminAccount());
        demoteButton.setVisible(picked.isAdminAccount());
        demoteButton.setManaged(picked.isAdminAccount());

        int count = TicketManager.getInstance().countTicketsFor(picked);
        ticketsLabel.setText("Tickets linked to this account: " + count);
    }

    @FXML
    protected void saveClicked() {
        String username = usernameField.getText().trim();
        String displayName = displayNameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String newPassword = newPasswordField.getText();

        AccountManager accounts = AccountManager.getInstance();

        List<String> errors = new ArrayList<>();

        if (!username.equals(account.getUserName())) {
            Account owner = accounts.findAccount(username);
            if (!InputValidator.isValidUsername(username)) {
                errors.add("Username: 3-16 characters, letters, numbers and underscores only.");
            } else if (owner != null && owner.getId() != account.getId()) {
                errors.add("Username: that username is already taken.");
            }
        }
        if (!displayName.equals(account.getDisplayName()) && !InputValidator.isValidDisplayName(displayName)) {
            errors.add("Display Name: needs at least one space (first and last name).");
        }
        if (!email.equals(account.getEmail()) && !InputValidator.isValidEmail(email)) {
            errors.add("Email: should look like name@site.com");
        }
        if (!phone.equals(account.getPhone()) && !InputValidator.isValidPhone(phone)) {
            errors.add("Phone: should look like 555-123-4567 or 5551234567.");
        }
        if (!newPassword.isEmpty() && !InputValidator.isValidPassword(newPassword)) {
            errors.add("New Password: 8-64 characters, no commas.");
        }

        if (!errors.isEmpty()) {
            errorDisplay.setText(String.join("\n", errors));
            return;
        }

        account.setUserName(username);
        account.setDisplayName(displayName);
        account.setEmail(email);
        account.setPhone(phone);
        accounts.saveAccounts();
        if (!newPassword.isEmpty()) {
            accounts.resetPassword(account, newPassword);
        }

        goToDashboard();
    }

    @FXML
    protected void promoteClicked() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Make " + account.getDisplayName() + " an administrator?\n\nAdmins can see and change every ticket and account. This cannot be undone from this screen.",
                ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText(null);
        if (confirm.showAndWait().orElse(ButtonType.NO) != ButtonType.YES) {
            return;
        }

        AccountManager accounts = AccountManager.getInstance();
        accounts.elevateToAdmin(account);
        loadAccount(accounts.findAccount(account.getId()));
    }

    @FXML
    protected void demoteClicked() {
        AccountManager accounts = AccountManager.getInstance();

        if (accounts.getLoggedIn() != null && accounts.getLoggedIn().getId() == account.getId()) {
            errorDisplay.setText("You cannot remove your own administrator access.");
            return;
        }
        if (accounts.getAllAdminAccounts().size() <= 1) {
            errorDisplay.setText("This is the last administrator, so it cannot be removed.");
            return;
        }

        int open = 0;
        for (Ticket t : ((AdminAccount) account).getAssignedTickets()) {
            Status s = t.getTicketStatus();
            if (s == Status.INPROGRESS || s == Status.UNASSIGNED || s == Status.PENDING) open++;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Remove administrator access from " + account.getDisplayName() + "?\n\n"
                        + open + " open ticket(s) assigned to them will become Unassigned. They will keep the tickets they opened.",
                ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText(null);
        if (confirm.showAndWait().orElse(ButtonType.NO) != ButtonType.YES) {
            return;
        }

        accounts.demoteToUser(account);
        loadAccount(accounts.findAccount(account.getId()));
    }

    @FXML
    protected void deleteClicked() {
        AccountManager accounts = AccountManager.getInstance();

        if (accounts.getLoggedIn() != null && accounts.getLoggedIn().getId() == account.getId()) {
            errorDisplay.setText("You cannot delete the account you are logged in with.");
            return;
        }
        int count = TicketManager.getInstance().countTicketsFor(account);
        if (count > 0) {
            errorDisplay.setText("This account is linked to " + count + " ticket(s). Reassign or close them before deleting it.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete the account \"" + account.getUserName() + "\"? This cannot be undone.",
                ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText(null);
        if (confirm.showAndWait().orElse(ButtonType.NO) != ButtonType.YES) {
            return;
        }

        accounts.deleteAccount(account);
        goToDashboard();
    }

    @FXML
    protected void backClicked() {
        goToDashboard();
    }

    private void goToDashboard() {
        SceneSwitcher.switchScene((Stage) errorDisplay.getScene().getWindow(), DASHBOARD);
    }
}
