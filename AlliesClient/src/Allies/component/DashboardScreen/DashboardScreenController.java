package Allies.component.DashboardScreen;

import Allies.component.DashboardScreen.AgentsDataArea.AgentListController;
import Allies.component.DashboardScreen.ContestDataArea.UboatListController;
import Allies.component.DashboardScreen.ContestDataArea.UboatString;
import Allies.component.main.AlliesMainController;
import Allies.main.util.Constants;
import com.sun.istack.internal.NotNull;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import okhttp3.*;
import utils.http.HttpClientUtil;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import static Allies.main.util.Constants.START_TASK_GENERATOR;
import static utils.Constants.REFRESH_RATE;
import static utils.Constants.SET_USER_READY;


public class DashboardScreenController{
    @FXML private AnchorPane uboatListComponent;
    @FXML private UboatListController uboatListComponentController;
    @FXML private AnchorPane agentListComponent;
    @FXML private AgentListController agentListComponentController;
    @FXML private Button joinContestButton;
    @FXML private Button readyButton;
    private AlliesMainController alliesMainController;
    @FXML private TextField missionSizeTextField;
    @FXML private Label errorMessageLabel;
    private final StringProperty errorMessageProperty = new SimpleStringProperty();
    private Timer timer;
    private TimerTask gameStatusRefresher;

    public void resetScreen() {
        joinContestButton.setDisable(false);
        readyButton.setDisable(true);
        missionSizeTextField.setText("");
        missionSizeTextField.setEditable(true);
    }

    @FXML
    public void initialize() {
        errorMessageLabel.textProperty().bind(errorMessageProperty);
    }
    public void setUboatMainController(AlliesMainController alliesMainController) {
        this.alliesMainController = alliesMainController;
    }

    private void updateGameStatus(Boolean isAllUsersReady) {
        Platform.runLater(() -> {
            if(isAllUsersReady){
                gameStatusRefresher.cancel();
            }
        });
    }

    public void gameOn() {
        String finalUrl = HttpUrl
                .parse(START_TASK_GENERATOR)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtil.runRequestWithBody(finalUrl, RequestBody.create(new byte[]{}), new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        System.out.println("Fail on Ally: Start task generator")
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBody = response.body().string();
                if (response.code() != 200) {
                    Platform.runLater(() ->
                            System.out.println("Something went wrong: " + responseBody)
                    );
                }
            }
        });
    }

    public void activeUboatListRefresher() {
        uboatListComponentController.startListRefresher();
    }

    @FXML void readyButtonClicked(ActionEvent event) {
        String finalUrl = HttpUrl
                .parse(SET_USER_READY)
                .newBuilder()
                .addQueryParameter("userType", "Allies")
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@org.jetbrains.annotations.NotNull Call call, @org.jetbrains.annotations.NotNull IOException e) {
                Platform.runLater(() ->
                        System.out.println("Fail on Ally: Ready button clicked")
                );
            }

            @Override
            public void onResponse(@org.jetbrains.annotations.NotNull Call call, @org.jetbrains.annotations.NotNull Response response) throws IOException {
                if (response.code() == 200) {
                    Platform.runLater(() -> {
                        alliesMainController.switchToContestScreen();
                        readyButton.setDisable(true);
                    });
                }
            }
        });
    }

    @FXML void joinContestButtonClicked(ActionEvent event) {
        try {
            int missionSize = Integer.parseInt(missionSizeTextField.getText());
            UboatString selectedUboat = uboatListComponentController.getUboatStringSelection();
            if (missionSize <= 0)
                errorMessageProperty.set("Please type an Integer greater than 0!");
            else if (selectedUboat == null) {
                errorMessageProperty.set("error! uboat selection must be done first!");
            } else {
                addAllyToUboatContest(selectedUboat, missionSize);
            }
        }
        catch (NumberFormatException e){
            errorMessageProperty.set("Please type an Integer greater than 0!");
        }
    }

    public void addAllyToUboatContest(UboatString selectedUboat, int missionSize) {
        String finalUrl = HttpUrl
                .parse(Constants.ADD_ALLIES_TO_UBOAT_CONTEST)
                .newBuilder()
                .addQueryParameter("battleFieldName", selectedUboat.getBattleFieldName())
                .addQueryParameter("allyMissionSize", String.valueOf(missionSize))
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        System.out.println("Fail on Ally: Add allies to uboat")
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    Platform.runLater(() ->
                            errorMessageProperty.set("Contest is full")
                    );
                } else {
                    Platform.runLater(() -> {
                        alliesMainController.activeSelectedContestDataRefresher();
                        alliesMainController.activeAlliesTeamsListRefresher();
                        joinContestButton.setDisable(true);
                        readyButton.setDisable(false);
                        missionSizeTextField.setEditable(false);
                    });
                }
            }
        });
    }

    public void activeAgentListRefresher() {
        agentListComponentController.startListRefresher();
    }
}
