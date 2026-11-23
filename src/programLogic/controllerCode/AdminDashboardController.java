package programLogic.controllerCode;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import programLogic.model.AccountManager;
import programLogic.model.AdminAccount;

/**
 * Provides the administrator dashboard controller entry point.
 */
public class AdminDashboardController {

    private enum View { ALL, ASSIGNED, UNASSIGNED, DRAFTS, USERS }

    private static View lastView = View.ALL;

    @FXML
    protected Label nameLabel;

    @FXML
    protected Node ticketView;

    @FXML
    protected AdminTicketListController ticketViewController;

    @FXML
    protected Node userView;

    @FXML
    protected AdminUserListController userViewController;

    @FXML
    protected void initialize() {
        AdminAccount loggedIn = (AdminAccount) AccountManager.getInstance().getLoggedIn();
        nameLabel.setText("    Logged In As:  " + loggedIn.getDisplayName());

        switch (lastView) {
            case ASSIGNED -> viewAssignedTickets();
            case UNASSIGNED -> viewUnassignedTickets();
            case DRAFTS -> viewDrafts();
            case USERS -> viewUserList();
            default -> viewAllTickets();
        }
    }

    @FXML
    protected void viewAssignedTickets() {
        lastView = View.ASSIGNED;
        showOnly(ticketView);
        ticketViewController.showAssignedToMe();
    }

    @FXML
    protected void viewAllTickets() {
        lastView = View.ALL;
        showOnly(ticketView);
        ticketViewController.showAll();
    }

    @FXML
    protected void viewUnassignedTickets() {
        lastView = View.UNASSIGNED;
        showOnly(ticketView);
        ticketViewController.showUnassigned();
    }

    @FXML
    protected void viewDrafts() {
        lastView = View.DRAFTS;
        showOnly(ticketView);
        ticketViewController.showDrafts();
    }

    @FXML
    protected void viewUserList() {
        lastView = View.USERS;
        userViewController.refresh();
        showOnly(userView);
    }

    @FXML
    protected void newTicketPressed() {
        SceneSwitcher.switchScene((Stage) nameLabel.getScene().getWindow(), "/resources/admin-add-ticket.fxml");
    }

    @FXML
    protected void logoutPressed() {
        Stage stage = (Stage) nameLabel.getScene().getWindow();
        AccountManager.getInstance().logout();
        lastView = View.ALL;
        SceneSwitcher.switchScene(stage, "/resources/login-screen.fxml");
    }

    private void showOnly(Node viewToShow) {
        for (Node view : new Node[]{ticketView, userView}) {
            boolean show = (view == viewToShow);
            view.setVisible(show);
            view.setManaged(show);
        }
    }
}
