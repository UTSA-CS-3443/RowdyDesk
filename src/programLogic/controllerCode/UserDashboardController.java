package programLogic.controllerCode;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Pagination;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import programLogic.model.Account;
import programLogic.model.AccountManager;
import programLogic.model.Status;
import programLogic.model.Ticket;
import programLogic.model.TicketManager;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles user ticket display and account-management actions.
 */
public class UserDashboardController {
    @FXML
    protected Label nameLabel;

    @FXML
    protected Label phoneLabel;

    @FXML
    protected Label emailLabel;

    // TODO ??? what this
    @FXML
    protected Label mysteryLabel;

    @FXML
    protected Button newTicketButton;

    @FXML
    protected ListView<String> ticketsListView;

    @FXML
    protected Pagination ticketsPagination;

    private ArrayList<Ticket> shown = new ArrayList<>();

    @FXML
    protected void initialize() {
        Account loggedIn = AccountManager.getInstance().getLoggedIn();

        nameLabel.setText(loggedIn.getDisplayName());
        phoneLabel.setText(loggedIn.getPhone());
        emailLabel.setText(loggedIn.getEmail());

        shown = new ArrayList<>(TicketManager.getInstance().getDraftsFor(loggedIn));
        shown.addAll(loggedIn.getTickets());

        ticketsPagination.setPageCount(Math.max(1, (int)Math.ceil(shown.size() / 12.0)));
        ticketsPagination.setPageFactory(page -> {
            List<Ticket> slice = shown.subList(Math.min(page * 12, shown.size()), Math.min(page * 12 + 12, shown.size()));
            ticketsListView.setItems(FXCollections.observableArrayList(
                    slice.stream()
                            .map(t -> String.format("#%s   %s   [%s]", Ticket.formatNumber(t.getId()), t.getShortDescription(), t.getTicketStatus()))
                            .toList()));
            return ticketsListView;
        });
    }

    @FXML
    protected void onNewTicketButtonClicked(MouseEvent event) {
        UserAddTicketController controller = (UserAddTicketController)SceneSwitcher.switchScene(event, "/resources/user-add-ticket.fxml");
        //if(controller != null) controller.loadTicket(1111);
    }

    @FXML
    protected void onTicketsListViewClicked(MouseEvent event) {
        int row = ticketsListView.getSelectionModel().getSelectedIndex();
        if (event.getClickCount() != 2 || row < 0) return;

        Ticket picked = shown.get(ticketsPagination.getCurrentPageIndex() * 12 + row);

        UserAddTicketController form = (UserAddTicketController)SceneSwitcher.switchScene(event, "/resources/user-add-ticket.fxml");
        if (form == null) return;

        if (picked.getTicketStatus() == Status.DRAFT) {
            form.loadDraft(picked);
        } else {
            form.loadTicket(picked.getId());
        }
    }

    @FXML
    protected void onEditAccountButtonClicked(Event event) {
        SceneSwitcher.switchScene(event, "/resources/edit-account-screen.fxml");
    }

    public static void viewUserTickets(Account account) {
        ArrayList<Ticket> tickets = account.getTickets();
        System.out.printf("You have %d tickets:\n___________________________________________\n", tickets.size());
        for(Ticket t : tickets) {
            System.out.printf("%s\n%s | Status: %s\n%s\n",
                    t.getShortDescription(),
                    new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(t.getTimeCreated()),
                    t.getTicketStatus(),
                    t.getLongDescription()
            );
            System.out.println("___________________________________________");
        }
    }

    public static boolean editUserAccount(AccountManager accountManager, Account account) {
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

    @FXML
    protected void logoutPressed() {
        Stage stage = (Stage) nameLabel.getScene().getWindow();
        AccountManager.getInstance().logout();
        SceneSwitcher.switchScene(stage, "/resources/login-screen.fxml");
    }
}
