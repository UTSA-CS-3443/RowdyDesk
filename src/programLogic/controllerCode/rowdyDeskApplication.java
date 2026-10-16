package programLogic.controllerCode;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

// test commit hi 2
// test commit hello from xolobarks
public class rowdyDeskApplication extends Application {

    public static void main(String[] args) {
        Application.launch(rowdyDeskApplication.class, args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                rowdyDeskApplication.class.getResource("/resources/login-screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 825, 475);
        primaryStage.setTitle("Hello!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
