package programLogic.controllerCode;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import programLogic.model.Account;
import programLogic.model.AccountManager;

public class AdminUserListController {

    @FXML
    protected TextField searchField;

    @FXML
    protected Label countLabel;

    @FXML
    protected TableView<Account> usersTable;

    @FXML
    protected TableColumn<Account, String> usernameCol;

    @FXML
    protected TableColumn<Account, String> displayNameCol;

    @FXML
    protected TableColumn<Account, String> emailCol;

    @FXML
    protected TableColumn<Account, String> phoneCol;

    @FXML
    protected TableColumn<Account, String> roleCol;

    private final ObservableList<Account> allUsers = FXCollections.observableArrayList();
    private final FilteredList<Account> filtered = new FilteredList<>(allUsers);
    private final SortedList<Account> sorted = new SortedList<>(filtered);

    @FXML
    protected void initialize() {
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("userName"));
        displayNameCol.setCellValueFactory(new PropertyValueFactory<>("displayName"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        roleCol.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().isAdminAccount() ? "Admin" : "User"));

        sorted.comparatorProperty().bind(usersTable.comparatorProperty());
        usersTable.setItems(sorted);

        usersTable.setRowFactory(table -> {
            TableRow<Account> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    modify(row.getItem());
                }
            });
            return row;
        });

        searchField.textProperty().addListener((obs, oldText, newText) -> {
            String q = newText.trim().toLowerCase();
            filtered.setPredicate(a -> q.isEmpty()
                    || contains(a.getUserName(), q)
                    || contains(a.getDisplayName(), q)
                    || contains(a.getEmail(), q));
            updateCount();
        });

        refresh();
    }

    public void refresh() {
        allUsers.setAll(AccountManager.getInstance().getAllAcounts());
        updateCount();
    }

    @FXML
    protected void modifySelected() {
        Account picked = usersTable.getSelectionModel().getSelectedItem();
        if (picked == null) {
            new Alert(Alert.AlertType.INFORMATION, "Click a user in the table first.").showAndWait();
            return;
        }
        modify(picked);
    }

    private void modify(Account picked) {
        AdminModifyUserController screen = (AdminModifyUserController) SceneSwitcher.switchScene(
                (Stage) usersTable.getScene().getWindow(), "/resources/admin-modify-user.fxml");
        if (screen != null) screen.loadAccount(picked);
    }

    private boolean contains(String text, String query) {
        return text != null && text.toLowerCase().contains(query);
    }

    private void updateCount() {
        int n = filtered.size();
        countLabel.setText(n + (n == 1 ? " user" : " users"));
    }
}
