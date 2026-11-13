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
     * @throws IOException On failed file load
     */
    public static Object switchScene(Event pivot, String fxmlFilename) throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneSwitcher.class.getResource(fxmlFilename));
        Parent root = loader.load();

        Stage stage = (Stage)((Node)pivot.getSource()).getScene().getWindow();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        return loader.getController();
    }
}
