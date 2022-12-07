package Allies.component.main;

import Allies.component.ContestScreen.ContestScreenController;
import Allies.component.LoginScreen.LoginScreenController;
import Allies.component.DashboardScreen.DashboardScreenController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

import static Allies.main.util.Constants.*;

public class AlliesMainController {
    @FXML private AnchorPane mainPanel;
    @FXML private Button DashboardButton;
    @FXML private Button ContestButton;
    @FXML private Label errorMessageLabel;
    @FXML private HBox buttonsHBox;
    private LoginScreenController loginScreenController;
    private DashboardScreenController dashboardScreenController;
    private ContestScreenController contestScreenController;
    private GridPane loginComponent;
    private BorderPane dashboardComponent;
    private BorderPane contestComponent;

    private Stage primaryStage;

    private final StringProperty errorMessageProperty = new SimpleStringProperty();

    public void setPrimaryStage(Stage stage){
        this.primaryStage = stage;
    }

    @FXML public void initialize() {
        loadLoginScreen();
        loadDashboardScreen();
        loadContestScreen();
    }

    @FXML
    void ContestButtonClicked(ActionEvent event) {
        switchToContestScreen();
    }
    @FXML
    void DashboardButtonClicked(ActionEvent event) {
        switchToDashboardScreen();
    }

    private void loadLoginScreen() {
        URL loginPageUrl = getClass().getResource(LOGIN_SCREEN_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPageUrl);
            loginComponent = fxmlLoader.load();
            loginScreenController = fxmlLoader.getController();
            loginScreenController.setAlliesMainController(this);
            setMainPanelTo(loginComponent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void loadDashboardScreen() {
        URL loginPageUrl = getClass().getResource(DASHBOARD_SCREEN_PAGE_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPageUrl);
            dashboardComponent = fxmlLoader.load();
            dashboardScreenController = fxmlLoader.getController();
            dashboardScreenController.setUboatMainController(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadContestScreen() {

        URL loginPageUrl = getClass().getResource(CONTEST_SCREEN_PAGE_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPageUrl);
            contestComponent = fxmlLoader.load();
            contestScreenController = fxmlLoader.getController();
            contestScreenController.setUboatMainController(this);
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

    public void switchToContestScreen(){
        setMainPanelTo(contestComponent);
    }

    public void switchToDashboardScreen() {
        setMainPanelTo(dashboardComponent);
    }

    public void setButtonHBoxVisible() {
        buttonsHBox.setVisible(true);
    }
    public void activeUboatListRefresher(){
        dashboardScreenController.activeUboatListRefresher();
    }

    public void activeSelectedContestDataRefresher() {
        contestScreenController.activeSelectedContestDataRefresher();
    }

    public void activeAlliesTeamsListRefresher() {
        contestScreenController.activeAlliesTeamsListRefresher();
    }

    public void activeAgentListRefresher() {
        dashboardScreenController.activeAgentListRefresher();
    }

    public void gameOn() {
        dashboardScreenController.gameOn();
    }

    public void resetDashboardSScreen() {
        dashboardScreenController.resetScreen();
        switchToDashboardScreen();
    }
}
