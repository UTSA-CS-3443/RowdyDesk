package programLogic.controllerCode;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneSwitcher {
    /**
     * Loads a new Scene from any class and optionally instantiate
     * its controller's instance
     *
     * @param pivot        Event used to access its Stage parent
     * @param fxmlFilename FXML file to load
     * @return The newly loaded scene's controller
     */
    public static Object switchScene(Event pivot, String fxmlFilename) {
        FXMLLoader loader = new FXMLLoader(SceneSwitcher.class.getResource(fxmlFilename));
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            System.out.println("Error loading scene " + fxmlFilename);
            System.out.println(e.getMessage());
            return null;
        }

        Stage stage = (Stage)((Node)pivot.getSource()).getScene().getWindow();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        return loader.getController();
    }

    public static Object switchScene(Stage stage, String fxmlFilename) {
        FXMLLoader loader = new FXMLLoader(SceneSwitcher.class.getResource(fxmlFilename));
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            System.out.println("Error loading scene " + fxmlFilename);
            System.out.println(e.getMessage());
            return null;
        }

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        return loader.getController();
    }
}
