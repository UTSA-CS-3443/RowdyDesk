package programLogic.controllerCode;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;
import programLogic.model.Account;
import programLogic.model.AccountManager;
import programLogic.model.AdminAccount;
import programLogic.model.TicketManager;
import programLogic.model.Ticket;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;


/**
 * Handles the command-line workflow for administrators creating support tickets.
 */
public class AdminAddTicketController  {
    TicketManager ticketManager;
    AccountManager accountManager;

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

        ObservableList<Account> usersList = FXCollections.observableArrayList(accountManager.getAllAcounts());
        ObservableList<AdminAccount> adminsList = FXCollections.observableList(accountManager.getAllAdminAccounts());
        adminsList.addFirst(null);

        userSelect.setItems(usersList);
        adminSelect.setItems(adminsList);
        category.setItems(FXCollections.observableArrayList(
                "category 1",
                "category 2",
                "category 3",
                "category 4"
        ));
        urgency.setItems(FXCollections.observableArrayList(
                3,
                2,
                1
        ));
        urgency.setValue(3);
    }


    @FXML
    public void submitClicked() {
        if(userSelect.getValue() == null || shortDescriptionField.getText().isBlank() || longDescriptionField.getText().isEmpty() || category.getValue()==null) {
            errorDisplay.setText("Please fill out all Required fields");
            return;
        }
        Ticket currTicket = ticketManager.createTicket(userSelect.getValue(), shortDescriptionField.getText(), longDescriptionField.getText());
        if(adminSelect.getValue() != null) ticketManager.assignTicket(currTicket, adminSelect.getValue());
        // TODO: add Urgency and Category
        SceneSwitcher.switchScene((Stage)errorDisplay.getScene().getWindow(), "/resources/admin-dashboard.fxml");
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
