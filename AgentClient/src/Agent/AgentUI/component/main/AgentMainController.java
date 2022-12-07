package Agent.AgentUI.component.main;

import Agent.AgentLogic.AgentLogic;
import Agent.AgentUI.component.ContestScreen.ContestScreenController;
import Agent.AgentUI.component.LoadAgentDetailsScreen.LoadAgentDetailsScreenController;
import Agent.AgentUI.component.LoginScreen.LoginScreenController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

import static Agent.AgentUI.main.utils.Constants.*;

public class AgentMainController {
    @FXML private AnchorPane mainPanel;
    private LoginScreenController loginScreenController;
    private LoadAgentDetailsScreenController loadAgentDetailsScreenController;
    private ContestScreenController contestScreenController;
    private GridPane loginComponent;
    private BorderPane loadAgentDetailsComponent;
    private BorderPane contestComponent;
    private Stage primaryStage;
    private AgentLogic agentLogic;

    private final StringProperty errorMessageProperty = new SimpleStringProperty();
    public void setPrimaryStage(Stage stage){
        this.primaryStage = stage;
    }

    @FXML public void initialize() {
        loadLoginScreen();
        loadLoadAgentDetailsScreen();
        loadContestScreen();
    }

    private void loadLoginScreen() {
        URL loginPageUrl = getClass().getResource(LOGIN_SCREEN_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPageUrl);
            loginComponent = fxmlLoader.load();
            loginScreenController = fxmlLoader.getController();
            loginScreenController.setAgentMainController(this);
            setMainPanelTo(loginComponent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void loadContestScreen() {

        URL loginPageUrl = getClass().getResource(CONTEST_SCREEN_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPageUrl);
            contestComponent = fxmlLoader.load();
            contestScreenController = fxmlLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void setMainPanelTo(Parent pane) {
        mainPanel.getChildren().clear();
        mainPanel.getChildren().add(pane);
        AnchorPane.setBottomAnchor(pane, 1.0);
        AnchorPane.setTopAnchor(pane, 1.0);
        AnchorPane.setLeftAnchor(pane, 1.0);
        AnchorPane.setRightAnchor(pane, 1.0);
    }
    public void activeAlliesTeamsListRefresher() {
        loadAgentDetailsScreenController.activeAlliesTeamsListRefresher();
    }
    public void switchToLoadAgentDetailsScreen() {
        setMainPanelTo(loadAgentDetailsComponent);
    }
    public void switchToContestScreen() {
        setMainPanelTo(contestComponent);
    }


    private void loadLoadAgentDetailsScreen() {
        URL loginPageUrl = getClass().getResource(LOAD_AGENT_DETAILS_SCREEN_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPageUrl);
            loadAgentDetailsComponent = fxmlLoader.load();
            loadAgentDetailsScreenController = fxmlLoader.getController();
            loadAgentDetailsScreenController.setAgentMainController(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setAgentLogic(AgentLogic agentLogic) {
        this.agentLogic = agentLogic;
        loadAgentDetailsScreenController.setAgentLogic(agentLogic);
        contestScreenController.setAgentLogic(agentLogic);
    }

    public void startContestDataAreaRefresher() {
        contestScreenController.startContestDataAreaRefresher();
    }

    public void setAllyTeamLabel(String allyTeamName) {
        contestScreenController.setAllyTeamLabel(allyTeamName);
    }

    public void setAgentName(String agentName) {
        agentLogic.setUserName(agentName);
    }
}
