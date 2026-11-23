package programLogic.controllerCode;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import programLogic.model.AccountManager;

import java.io.IOException;
import java.util.Map;
import java.util.Stack;

public class SceneSwitcher {
    public static Stack<String> navigationStack = new Stack<>();

    private static final Map<String, String> TITLES = Map.of(
            "/resources/login-screen.fxml", "RowdyDesk - Login Screen",
            "/resources/edit-account-screen.fxml", "RowdyDesk - Edit Account",
            "/resources/user-dashboard.fxml", "RowdyDesk - My Tickets",
            "/resources/user-add-ticket.fxml", "RowdyDesk - New Ticket",
            "/resources/admin-dash.fxml", "RowdyDesk - Admin - Dashboard",
            "/resources/admin-add-ticket.fxml", "RowdyDesk - Admin - New Ticket",
            "/resources/admin-ticket-detail.fxml", "RowdyDesk - Admin - Ticket Details",
            "/resources/admin-modify-user.fxml", "RowdyDesk - Admin - Modify User");

    /**
     * Loads a new Scene from any class and optionally instantiate
     * its controller's instance
     *
     * @param pivot        Event used to access its Stage parent
     * @param fxmlFilename FXML file to load
     * @return The newly loaded scene's controller
     */
    public static Object switchScene(Event pivot, String fxmlFilename) {
        return switchScene((Stage)((Node)pivot.getSource()).getScene().getWindow(), fxmlFilename);
    }

    public static Object switchScene(Stage stage, String fxmlFilename) {
        FXMLLoader loader = new FXMLLoader(SceneSwitcher.class.getResource(fxmlFilename));
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            System.out.println("Error loading scene " + fxmlFilename);
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }

        Scene scene = new Scene(root);
        stage.setTitle(TITLES.getOrDefault(fxmlFilename, "RowdyDesk"));
        stage.setScene(scene);
        stage.show();
        navigationStack.push(fxmlFilename);
        return loader.getController();
    }

    public static Object backScene(Event pivot) {
        if(navigationStack.isEmpty()) {
            return switchScene(pivot, "/resources/login-screen.fxml");
        } else {
            return switchScene(pivot, navigationStack.pop());
        }
    }

    public static void logout(Event pivot) {
        AccountManager.getInstance().logout();
        while(navigationStack.isEmpty()) backScene(pivot);
    }
}
