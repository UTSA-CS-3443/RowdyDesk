package programLogic.controllerCode;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Pagination;
import javafx.stage.Stage;
import org.w3c.dom.events.Event;
import programLogic.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides the administrator dashboard controller entry point.
 */
public class AdminDashboardController {
    TicketManager ticketManager;

    @FXML
    protected Label nameLabel;

    @FXML
    protected ListView<String> ticketsListView;

    @FXML
    protected Pagination ticketsPagination;

    @FXML
    protected void initialize() {
        ticketManager = TicketManager.getInstance();
        AdminAccount loggedIn = (AdminAccount)AccountManager.getInstance().getLoggedIn();

        ArrayList<Ticket> tickets = loggedIn.getAssignedTickets();

        nameLabel.setText("    Logged In As:  " + loggedIn.getDisplayName());
//        phoneLabel.setText(loggedIn.getPhone());
//        emailLabel.setText(loggedIn.getEmail());

        viewAssignedTickets();
    }

    @FXML
    protected void viewAssignedTickets() {
        AdminAccount loggedIn = (AdminAccount)AccountManager.getInstance().getLoggedIn();
        ArrayList<Ticket> tickets = loggedIn.getAssignedTickets();

        ticketsPagination.setPageCount(Math.max(1, (int)Math.ceil(tickets.size() / 12.0)));
        ticketsPagination.setPageFactory(page -> {
            ticketsListView.getItems().clear();
            List<Ticket> ticketsSlice = tickets.subList(page * 12, Math.min(page * 12 + 12, tickets.size()));
            ObservableList<String> titles = FXCollections.observableArrayList(ticketsSlice.stream().map(Ticket::getShortDescription).toArray(String[]::new));
            ticketsListView.getItems().addAll(titles);
            return ticketsListView;
        });
    }

    @FXML
    protected void viewAllTickets() {
        ArrayList<Ticket> tickets = ticketManager.getActiveTickets();

        ticketsPagination.setPageCount(Math.max(1, (int)Math.ceil(tickets.size() / 12.0)));
        ticketsPagination.setPageFactory(page -> {
            ticketsListView.getItems().clear();
            List<Ticket> ticketsSlice = tickets.subList(page * 12, Math.min(page * 12 + 12, tickets.size()));
            ObservableList<String> titles = FXCollections.observableArrayList(ticketsSlice.stream().map(Ticket::getShortDescription).toArray(String[]::new));
            ticketsListView.getItems().addAll(titles);
            return ticketsListView;
        });
    }

    @FXML
    protected void viewYourTickets() {
        AdminAccount loggedIn = (AdminAccount)AccountManager.getInstance().getLoggedIn();
        ArrayList<Ticket> tickets = loggedIn.getTickets();

        ticketsPagination.setPageCount(Math.max(1, (int)Math.ceil(tickets.size() / 12.0)));
        ticketsPagination.setPageFactory(page -> {
            ticketsListView.getItems().clear();
            List<Ticket> ticketsSlice = tickets.subList(page * 12, Math.min(page * 12 + 12, tickets.size()));
            ObservableList<String> titles = FXCollections.observableArrayList(ticketsSlice.stream().map(Ticket::getShortDescription).toArray(String[]::new));
            ticketsListView.getItems().addAll(titles);
            return ticketsListView;
        });
    }

    @FXML
    protected void newTicketPressed() {
        SceneSwitcher.switchScene((Stage) nameLabel.getScene().getWindow(), "/resources/admin-add-ticket.fxml");

    }

    @FXML
    protected void logoutPressed() {
        SceneSwitcher.switchScene((Stage) nameLabel.getScene().getWindow(), "/resources/login-screen.fxml");
    }
}
