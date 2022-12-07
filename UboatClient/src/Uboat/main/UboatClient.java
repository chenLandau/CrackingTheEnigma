package Uboat.main;

import Uboat.component.main.UboatMainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

import static Uboat.main.utils.Constants.MAIN_PAGE_FXML_RESOURCE_LOCATION;

public class UboatClient extends Application {
    private UboatMainController uboatMainController;

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(600);
        primaryStage.setTitle("Uboat App");

        URL loginPage = getClass().getResource(MAIN_PAGE_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPage);
            Parent root = fxmlLoader.load();
            uboatMainController = fxmlLoader.getController();
            uboatMainController.setPrimaryStage(primaryStage);

            Scene scene = new Scene(root, 1400, 900);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
