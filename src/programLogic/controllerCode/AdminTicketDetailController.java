package programLogic.controllerCode;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import programLogic.model.AdminAccount;
import programLogic.model.Status;
import programLogic.model.Ticket;
import programLogic.model.TicketManager;

public class AdminTicketDetailController {

    private static final String DASHBOARD = "/resources/admin-dash.fxml";

    private Ticket ticket;

    @FXML
    protected Label titleLabel;

    @FXML
    protected TextField shortDescriptionField;

    @FXML
    protected TextArea longDescriptionField;

    @FXML
    protected Label openedForLabel;

    @FXML
    protected Label createdLabel;

    @FXML
    protected ChoiceBox<Status> statusSelect;

    @FXML
    protected ChoiceBox<AdminAccount> adminSelect;

    @FXML
    protected Label errorDisplay;

    @FXML
    protected void initialize() {
        statusSelect.setItems(FXCollections.observableArrayList(
                Status.PENDING, Status.UNASSIGNED, Status.INPROGRESS, Status.RESOLVED));
        statusSelect.setConverter(new StringConverter<Status>() {
            @Override
            public String toString(Status status) {
                return TicketDisplay.statusText(status);
            }

            @Override
            public Status fromString(String text) {
                return null;
            }
        });

        adminSelect.setItems(TicketFormOptions.adminChoices());
        adminSelect.setConverter(TicketFormOptions.adminConverter());
    }

    public void loadTicket(Ticket picked) {
        ticket = picked;

        titleLabel.setText("Ticket #" + Ticket.formatNumber(picked.getId()));
        shortDescriptionField.setText(picked.getShortDescription());
        longDescriptionField.setText(picked.getLongDescription());
        openedForLabel.setText(picked.getUser() == null ? "-" : TicketFormOptions.describe(picked.getUser()));
        createdLabel.setText(TicketDisplay.dateTime(picked.getTimeCreated()));
        statusSelect.setValue(picked.getTicketStatus());

        AdminAccount current = picked.getAssignedTo();
        AdminAccount match = TicketFormOptions.NO_ADMIN;
        for (AdminAccount admin : adminSelect.getItems()) {
            if (current != null && admin.getId() == current.getId()) match = admin;
        }
        adminSelect.setValue(match);
    }

    @FXML
    protected void saveClicked() {
        if (ticket == null) return;

        if (shortDescriptionField.getText().isBlank() || longDescriptionField.getText().isBlank()) {
            errorDisplay.setText("The short description and the description cannot be empty.");
            return;
        }

        AdminAccount oldAdmin = ticket.getAssignedTo();
        AdminAccount newAdmin = TicketFormOptions.adminOrNull(adminSelect.getValue());
        boolean assignmentChanged = (oldAdmin == null ? -1 : oldAdmin.getId()) != (newAdmin == null ? -1 : newAdmin.getId());

        Status oldStatus = ticket.getTicketStatus();
        Status chosenStatus = statusSelect.getValue();
        boolean statusTouched = chosenStatus != oldStatus;

        Status finalStatus = oldStatus;
        if (statusTouched) {
            finalStatus = chosenStatus;
        } else if (assignmentChanged) {
            finalStatus = (newAdmin == null) ? Status.UNASSIGNED : Status.INPROGRESS;
        }

        if (newAdmin == null && finalStatus == Status.INPROGRESS) {
            errorDisplay.setText("A ticket that is In Progress needs an admin. Pick one in Assigned To.");
            return;
        }
        if (newAdmin != null && finalStatus == Status.UNASSIGNED) {
            errorDisplay.setText("A ticket with an admin assigned cannot be Unassigned. Change one of them.");
            return;
        }

        TicketManager tm = TicketManager.getInstance();
        ticket.setShortDescription(shortDescriptionField.getText());
        ticket.setLongDescription(longDescriptionField.getText());

        if (assignmentChanged) {
            tm.assignTicket(ticket, newAdmin);
        }
        if (finalStatus == Status.RESOLVED) {
            tm.resolveTicket(ticket);
        } else {
            ticket.setTicketStatus(finalStatus);
        }
        tm.saveDataToFile();

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
