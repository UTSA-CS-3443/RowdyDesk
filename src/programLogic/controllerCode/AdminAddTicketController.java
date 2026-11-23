package programLogic.controllerCode;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import programLogic.model.Account;
import programLogic.model.AccountManager;
import programLogic.model.AdminAccount;
import programLogic.model.Ticket;
import programLogic.model.TicketManager;

/**
 * Handles the command-line workflow for administrators creating support tickets.
 */
public class AdminAddTicketController {

    private static final String DASHBOARD = "/resources/admin-dash.fxml";

    private TicketManager ticketManager;
    private AccountManager accountManager;

    private Ticket currentDraft;

    @FXML
    protected Label titleLabel;

    @FXML
    protected TextField shortDescriptionField;

    @FXML
    protected TextArea longDescriptionField;

    @FXML
    protected Label errorDisplay;

    @FXML
    protected ChoiceBox<String> category;

    @FXML
    protected ChoiceBox<Integer> urgency;

    @FXML
    protected ChoiceBox<Account> userSelect;

    @FXML
    protected ChoiceBox<AdminAccount> adminSelect;

    @FXML
    protected void initialize() {
        ticketManager = TicketManager.getInstance();
        accountManager = AccountManager.getInstance();

        category.setItems(FXCollections.observableArrayList(TicketFormOptions.CATEGORIES));

        urgency.setItems(FXCollections.observableArrayList(TicketFormOptions.URGENCIES));
        urgency.setConverter(TicketFormOptions.urgencyConverter());
        urgency.setValue(TicketFormOptions.DEFAULT_URGENCY);

        userSelect.setItems(FXCollections.observableArrayList(accountManager.getAllAcounts()));
        userSelect.setConverter(TicketFormOptions.accountConverter());

        adminSelect.setItems(TicketFormOptions.adminChoices());
        adminSelect.setConverter(TicketFormOptions.adminConverter());
        adminSelect.setValue(TicketFormOptions.NO_ADMIN);
    }

    public void loadDraft(Ticket draft) {
        currentDraft = draft;
        titleLabel.setText("Draft Ticket #" + Ticket.formatNumber(draft.getId()));

        shortDescriptionField.setText(draft.getShortDescription());
        longDescriptionField.setText(draft.getLongDescription());
        userSelect.setValue(draft.getUser());
        adminSelect.setValue(draft.getAssignedTo() == null ? TicketFormOptions.NO_ADMIN : draft.getAssignedTo());
    }

    @FXML
    protected void submitClicked() {
        Account openedFor = userSelect.getValue();

        if (openedFor == null
                || shortDescriptionField.getText().isBlank()
                || longDescriptionField.getText().isBlank()
                || category.getValue() == null) {
            errorDisplay.setText("* - Please fill out all required fields");
            return;
        }

        AdminAccount assignee = selectedAdminOrNull();

        if (currentDraft != null) {
            ticketManager.submitDraft(currentDraft, openedFor, assignee,
                    shortDescriptionField.getText(), longDescriptionField.getText());
        } else {
            Ticket ticket = ticketManager.createTicket(openedFor,
                    shortDescriptionField.getText(), longDescriptionField.getText());
            if (assignee != null) {
                ticketManager.assignTicket(ticket, assignee);
            }
        }
        // TODO: add Urgency and Category

        goToDashboard();
    }

    @FXML
    protected void saveDraftClicked() {
        if (shortDescriptionField.getText().isBlank()) {
            errorDisplay.setText("Add a short description so you can find this draft later.");
            return;
        }

        ticketManager.saveDraft(currentDraft,
                accountManager.getLoggedIn(),
                userSelect.getValue(),
                selectedAdminOrNull(),
                shortDescriptionField.getText(),
                longDescriptionField.getText());

        goToDashboard();
    }

    @FXML
    protected void discardClicked() {
        if (currentDraft != null) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                    TicketFormOptions.discardDraftMessage(currentDraft.getId()),
                    ButtonType.YES, ButtonType.NO);
            confirm.setHeaderText(null);
            if (confirm.showAndWait().orElse(ButtonType.NO) != ButtonType.YES) {
                return;
            }
            ticketManager.discardDraft(currentDraft);
        }
        goToDashboard();
    }

    private AdminAccount selectedAdminOrNull() {
        AdminAccount chosen = adminSelect.getValue();
        return (chosen == null || chosen == TicketFormOptions.NO_ADMIN) ? null : chosen;
    }

    private void goToDashboard() {
        SceneSwitcher.switchScene((Stage) errorDisplay.getScene().getWindow(), DASHBOARD);
    }
//    public static void createTicket(TicketManager ticketManager, AccountManager accountManager, AdminAccount thisAccount) {
//
//        System.out.println("Please enter the username of the user");
//        Account user;
//        while(true) {
//            user = accountManager.findAccount(InputValidator.readString());
//
//
//            if(user != null) {
//                break;
//            }
//            System.out.println("Error, user not found. Enter 'guest' if user is unknown.");
//        }
//
//        System.out.println("Please enter a short description of the issue: ");
//        String shortDesc = InputValidator.readLine();
//
//
//        System.out.println("Now you may enter a more specific description of the issue");
//        String longDesc = InputValidator.readLine();
//
//        Ticket newTicket = ticketManager.createTicket(user, shortDesc, longDesc);
//        System.out.println("Ticket has been successfully created");
//
//        System.out.println("Assign this ticket to you?");
//        boolean assignToThisAdmin = InputValidator.readBooleanValue("please enter either Yes or No");
//
//        if (assignToThisAdmin) {
//            ticketManager.assignTicket(newTicket, thisAccount);
//            System.out.println("Ticket has been assigned to you!");
//        }
//    }
}
