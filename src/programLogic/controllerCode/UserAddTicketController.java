package programLogic.controllerCode;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import programLogic.model.Account;
import programLogic.model.AccountManager;
import programLogic.model.Ticket;
import programLogic.model.TicketManager;


/**
 * Handles the command-line workflow for users creating support tickets.
 */
public class UserAddTicketController {
    @FXML
    protected TextField shortDescriptionField;

    @FXML
    protected TextField categoryField;

    @FXML
    protected TextField typeField;

    @FXML
    protected TextArea descriptionField;

    @FXML
    protected Button submitButton;

    @FXML
    protected void onSubmitButtonClicked(MouseEvent event) {
        String shortDescription = shortDescriptionField.getText();
        String category = categoryField.getText();
        String type = typeField.getText();
        String description = descriptionField.getText();

        TicketManager.getInstance().createTicket(AccountManager.getInstance().getLoggedIn(), shortDescription, description);
        SceneSwitcher.switchScene(event, "/resources/user-dashboard.fxml");
    }

    public void loadTicket(int id) {
        Ticket ticket = TicketManager.getInstance().findTicket(id);

        shortDescriptionField.setText(ticket.getShortDescription());
        descriptionField.setText(ticket.getLongDescription());

        shortDescriptionField.setEditable(false);
        descriptionField.setEditable(false);
    }

    public static void createTicket(TicketManager ticketManager, Account account) {
        System.out.print("Enter a brief description (~5 words or less): ");
        String shortDesc = InputValidator.readLine();
        System.out.print("Enter a detailed description (with no line breaks): ");
        String longDesc = InputValidator.readLine();

        ticketManager.createTicket(account, shortDesc, longDesc);

    }
}
