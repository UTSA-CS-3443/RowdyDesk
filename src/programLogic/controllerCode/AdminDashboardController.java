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
    private ArrayList<Ticket> ticketsViewing;

    @FXML
    protected Label nameLabel;

    @FXML
    protected ListView<String> ticketsListView;

    @FXML
    protected Pagination ticketsPagination;

    @FXML
    protected void initialize() {
        AdminAccount loggedIn = (AdminAccount)AccountManager.getInstance().getLoggedIn();
        nameLabel.setText("    Logged In As:  " + loggedIn.getDisplayName());

        ticketsPagination.setPageFactory(page -> {
            ticketsListView.getItems().clear();
            List<Ticket> ticketsSlice = ticketsViewing.subList(page * 12, Math.min(page * 12 + 12, ticketsViewing.size()));
            ObservableList<String> titles = FXCollections.observableArrayList(ticketsSlice.stream().map(Ticket::getShortDescription).toArray(String[]::new));
            ticketsListView.getItems().addAll(titles);
            return ticketsListView;
        });

        viewAssignedTickets();
    }

    @FXML
    protected void viewAssignedTickets() {
        updateTicketsListView(((AdminAccount)AccountManager.getInstance().getLoggedIn()).getAssignedTickets());
    }

    @FXML
    protected void viewAllTickets() {
        updateTicketsListView(TicketManager.getInstance().getActiveTickets());
    }

    @FXML
    protected void viewYourTickets() {
        updateTicketsListView(AccountManager.getInstance().getLoggedIn().getTickets());
    }

    @FXML
    protected void newTicketPressed() {
        SceneSwitcher.switchScene((Stage) nameLabel.getScene().getWindow(), "/resources/admin-add-ticket.fxml");

    }

    @FXML
    protected void logoutPressed() {
        SceneSwitcher.switchScene((Stage) nameLabel.getScene().getWindow(), "/resources/login-screen.fxml");
    }

    private void updateTicketsListView(ArrayList<Ticket> newTicketsList) {
        ticketsViewing = newTicketsList;
        ticketsPagination.setPageCount(Math.max(1, (int)Math.ceil(ticketsViewing.size() / 12.0)));
        ticketsPagination.setCurrentPageIndex(0);
    }
}
