package programLogic.controllerCode;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import programLogic.model.Account;
import programLogic.model.AccountManager;
import programLogic.model.Ticket;
import programLogic.model.TicketManager;

/**
 * Handles the command-line workflow for users creating support tickets.
 */
public class UserAddTicketController {

    private static final String DASHBOARD = "/resources/user-dashboard.fxml";

    private Ticket currentDraft;

    @FXML
    protected Label titleLabel;

    @FXML
    protected TextField shortDescriptionField;

    @FXML
    protected ChoiceBox<String> category;

    @FXML
    protected ChoiceBox<Account> userSelect;

    @FXML
    protected TextArea longDescriptionField;

    @FXML
    protected Label errorDisplay;

    @FXML
    protected Button submitButton;

    @FXML
    protected Button saveDraftButton;

    @FXML
    protected Button discardButton;

    @FXML
    protected void initialize() {
        Account me = AccountManager.getInstance().getLoggedIn();

        category.setItems(FXCollections.observableArrayList(TicketFormOptions.CATEGORIES));

        userSelect.setItems(FXCollections.observableArrayList(me));
        userSelect.setConverter(TicketFormOptions.accountConverter());
        userSelect.setValue(me);
        userSelect.setDisable(true);
    }

    @FXML
    protected void submitClicked() {
        if (shortDescriptionField.getText().isBlank()
                || longDescriptionField.getText().isBlank()
                || category.getValue() == null) {
            errorDisplay.setText("* - Please fill out all required fields");
            return;
        }

        Account me = AccountManager.getInstance().getLoggedIn();

        if (currentDraft != null) {
            TicketManager.getInstance().submitDraft(currentDraft, me, null,
                    shortDescriptionField.getText(), longDescriptionField.getText());
        } else {
            TicketManager.getInstance().createTicket(me,
                    shortDescriptionField.getText(), longDescriptionField.getText());
        }

        goToDashboard();
    }

    @FXML
    protected void saveDraftClicked() {
        if (shortDescriptionField.getText().isBlank()) {
            errorDisplay.setText("Add a short description so you can find this draft later.");
            return;
        }

        Account me = AccountManager.getInstance().getLoggedIn();
        TicketManager.getInstance().saveDraft(currentDraft, me, me, null,
                shortDescriptionField.getText(), longDescriptionField.getText());

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
            TicketManager.getInstance().discardDraft(currentDraft);
        }
        goToDashboard();
    }

    public void loadDraft(Ticket draft) {
        currentDraft = draft;
        titleLabel.setText("Draft Ticket #" + Ticket.formatNumber(draft.getId()));

        shortDescriptionField.setText(draft.getShortDescription());
        longDescriptionField.setText(draft.getLongDescription());
    }

    public void loadTicket(int id) {
        Ticket ticket = TicketManager.getInstance().findTicket(id);
        if (ticket == null) return;

        titleLabel.setText("Ticket #" + Ticket.formatNumber(ticket.getId()));
        shortDescriptionField.setText(ticket.getShortDescription());
        longDescriptionField.setText(ticket.getLongDescription());
        if (ticket.getUser() != null) {
            userSelect.setItems(FXCollections.observableArrayList(ticket.getUser()));
            userSelect.setValue(ticket.getUser());
        }

        shortDescriptionField.setEditable(false);
        longDescriptionField.setEditable(false);
        category.setDisable(true);
        submitButton.setVisible(false);
        saveDraftButton.setVisible(false);
        discardButton.setText("Close");
    }

    private void goToDashboard() {
        SceneSwitcher.switchScene((Stage) errorDisplay.getScene().getWindow(), DASHBOARD);
    }
}
