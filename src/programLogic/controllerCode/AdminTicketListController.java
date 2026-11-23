package programLogic.controllerCode;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import programLogic.model.AccountManager;
import programLogic.model.AdminAccount;
import programLogic.model.Ticket;
import programLogic.model.TicketManager;
import programLogic.model.Status;

import java.util.ArrayList;
import java.util.List;

public class AdminTicketListController {

    @FXML
    protected Label headingLabel;

    @FXML
    protected TextField searchField;

    @FXML
    protected Label countLabel;

    @FXML
    protected TableView<Ticket> ticketsTable;

    @FXML
    protected TableColumn<Ticket, String> numberCol;

    @FXML
    protected TableColumn<Ticket, String> shortCol;

    @FXML
    protected TableColumn<Ticket, String> statusCol;

    @FXML
    protected TableColumn<Ticket, String> openedForCol;

    @FXML
    protected TableColumn<Ticket, String> assignedCol;

    @FXML
    protected TableColumn<Ticket, String> createdCol;

    private final ObservableList<Ticket> shown = FXCollections.observableArrayList();
    private final FilteredList<Ticket> filtered = new FilteredList<>(shown);
    private final SortedList<Ticket> sorted = new SortedList<>(filtered);

    @FXML
    protected void initialize() {
        numberCol.setCellValueFactory(cell -> new SimpleStringProperty(TicketDisplay.number(cell.getValue())));
        shortCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getShortDescription()));
        statusCol.setCellValueFactory(cell -> new SimpleStringProperty(TicketDisplay.statusText(cell.getValue().getTicketStatus())));
        openedForCol.setCellValueFactory(cell -> new SimpleStringProperty(TicketDisplay.person(cell.getValue().getUser(), "-")));
        assignedCol.setCellValueFactory(cell -> new SimpleStringProperty(TicketDisplay.person(cell.getValue().getAssignedTo(), "Unassigned")));
        createdCol.setCellValueFactory(cell -> new SimpleStringProperty(TicketDisplay.date(cell.getValue().getTimeCreated())));

        sorted.comparatorProperty().bind(ticketsTable.comparatorProperty());
        ticketsTable.setItems(sorted);

        ticketsTable.setRowFactory(table -> {
            TableRow<Ticket> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    open(row.getItem());
                }
            });
            return row;
        });

        searchField.textProperty().addListener((obs, oldText, newText) -> {
            String q = newText.trim().toLowerCase();
            filtered.setPredicate(t -> q.isEmpty()
                    || contains(TicketDisplay.number(t), q)
                    || contains(t.getShortDescription(), q)
                    || contains(TicketDisplay.statusText(t.getTicketStatus()), q)
                    || contains(TicketDisplay.person(t.getUser(), ""), q)
                    || contains(TicketDisplay.person(t.getAssignedTo(), "Unassigned"), q));
            updateCount();
        });
    }

    public void showAll() {
        show("All Tickets", TicketManager.getInstance().getActiveTickets());
    }

    public void showAssignedToMe() {
        AdminAccount me = (AdminAccount) AccountManager.getInstance().getLoggedIn();
        show("Assigned To You", me.getAssignedTickets());
    }

    public void showUnassigned() {
        List<Ticket> unassigned = new ArrayList<>();
        for (Ticket t : TicketManager.getInstance().getActiveTickets()) {
            if (t.getAssignedTo() == null) unassigned.add(t);
        }
        show("Unassigned Tickets", unassigned);
    }

    public void showDrafts() {
        show("My Drafts", TicketManager.getInstance().getDraftsFor(AccountManager.getInstance().getLoggedIn()));
    }

    private void show(String heading, List<Ticket> tickets) {
        headingLabel.setText(heading);
        shown.setAll(tickets);
        updateCount();
    }

    @FXML
    protected void openSelected() {
        Ticket picked = ticketsTable.getSelectionModel().getSelectedItem();
        if (picked == null) {
            new Alert(Alert.AlertType.INFORMATION, "Click a ticket in the table first.").showAndWait();
            return;
        }
        open(picked);
    }

    private void open(Ticket picked) {
        Stage stage = (Stage) ticketsTable.getScene().getWindow();

        if (picked.getTicketStatus() == Status.DRAFT) {
            AdminAddTicketController form =
                    (AdminAddTicketController) SceneSwitcher.switchScene(stage, "/resources/admin-add-ticket.fxml");
            if (form != null) form.loadDraft(picked);
        } else {
            AdminTicketDetailController detail =
                    (AdminTicketDetailController) SceneSwitcher.switchScene(stage, "/resources/admin-ticket-detail.fxml");
            if (detail != null) detail.loadTicket(picked);
        }
    }

    private boolean contains(String text, String query) {
        return text != null && text.toLowerCase().contains(query);
    }

    private void updateCount() {
        int n = filtered.size();
        countLabel.setText(n + (n == 1 ? " ticket" : " tickets"));
    }
}
