package Agent.AgentUI.main;

import Agent.AgentLogic.AgentLogic;
import Agent.AgentUI.component.main.AgentMainController;
import Agent.AgentUI.main.utils.Constants;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;


public class AgentClient extends Application {
    private AgentMainController agentMainController;

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(600);
        primaryStage.setTitle("Agent App");

        URL loginPage = getClass().getResource(Constants.MAIN_PAGE_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPage);
            Parent root = fxmlLoader.load();
            agentMainController = fxmlLoader.getController();
            AgentLogic agentLogic = new AgentLogic();
            agentMainController.setAgentLogic(agentLogic);
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
