package Uboat.component.main;

import Uboat.component.ContestScreen.ContestScreenController;
import Uboat.component.LoginScreen.LoginScreenController;
import Uboat.component.MachineScreen.MachineScreenController;
import com.sun.istack.internal.NotNull;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import okhttp3.*;
import utils.http.HttpClientUtil;

import java.io.IOException;
import java.net.URL;

import static Uboat.main.utils.Constants.*;

public class UboatMainController {
    @FXML private TextField filePathTextField;
    @FXML private AnchorPane mainPanel;
    @FXML private Button machineButton;
    @FXML private Button ContestButton;
    @FXML private HBox buttonsHbox;
    private LoginScreenController loginScreenController;
    private MachineScreenController machineScreenController;
    private ContestScreenController contestScreenController;
    private GridPane loginComponent;
    private GridPane machineComponent;
    private GridPane contestComponent;
    private final StringProperty currentUserNameProperty = new SimpleStringProperty();

    public void setPrimaryStage(Stage stage){
        loginScreenController.setPrimaryStage(stage);
    }
    @FXML void ContestButtonClicked(ActionEvent event) {
        switchToContestScreen();
    }

    @FXML void machineButtonClicked(ActionEvent event) {
        switchToMachineScreen();
    }

    @FXML
    public void initialize() {
        loadLoginScreen();
        loadMachineScreen();
        loadContestScreen();
    }

    private void loadLoginScreen() {
        URL loginPageUrl = getClass().getResource(LOGIN_SCREEN_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPageUrl);
            loginComponent = fxmlLoader.load();
            loginScreenController = fxmlLoader.getController();
            loginScreenController.setUboatMainController(this);
            setMainPanelTo(loginComponent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadMachineScreen() {
        URL loginPageUrl = getClass().getResource(MACHINE_SCREEN_PAGE_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPageUrl);
            machineComponent = fxmlLoader.load();
            machineScreenController = fxmlLoader.getController();
            machineScreenController.setUboatMainController(this);
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
    public void switchToLoginScreen(){
        setMainPanelTo(loginComponent);
    }
    public void activeAlliesTeamsListRefresher() {
        contestScreenController.activeAlliesTeamsListRefresher();
    }

    public void switchToMachineScreen() {
        setMainPanelTo(machineComponent);
    }

    public void codeConfigurationChanged() {
        machineScreenController.setCodeConfigurationStackPane();
        contestScreenController.codeConfigurationChanged();
    }

    public void readyButtonClicked() {
        machineScreenController.readyButtonClicked();
    }

    public void loadButtonClicked() {
        buttonsHbox.setVisible(true);
        machineScreenController.LoadFileClicked();
        contestScreenController.setDictionaryWords();
        activeAlliesTeamsListRefresher();
    }

    public void addDecryptedString() {
        machineScreenController.addDecryptedString();
    }

    public void logOutButtonClicked() {
        String finalUrl = HttpUrl
                .parse(LOG_OUT)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtil.runRequestWithBody(finalUrl, RequestBody.create(new byte[]{}), new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        System.out.println("Fail on uboat: logout")
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBody = response.body().string();
                if (response.code() != 200) {
                    Platform.runLater(() ->
                            System.out.println("Something went wrong: " + responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        switchToLoginScreen();
                        buttonsHbox.setVisible(false);
                        loginScreenController.resetScreen();
                        machineScreenController.reset();
                    });

                }
            }
        });
    }

    public void resetCurrentContest() {
        machineScreenController.resetCurrentContest();
    }
}
