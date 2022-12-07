package Agent.AgentUI.component.ContestScreen.ContestAndTeamDataArea.ContestDataArea;

import Agent.AgentUI.component.ContestScreen.ContestAndTeamDataArea.ContestAndTeamDataAreaController;
import DataTransferObject.UboatDTO;
import com.sun.istack.internal.NotNull;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import utils.http.HttpClientUtil;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import static Agent.AgentUI.main.utils.Constants.GET_ENIGMA_MACHINE;
import static utils.Constants.*;

public class ContestDataAreaController {
    @FXML private Label battleFieldNameLabel;
    @FXML private Label uboatUsernameLabel;
    @FXML private Label gameStatusLabel;
    @FXML private Label gameLevelLabel;
    @FXML private Label activeTeamLabel;
    private Timer timer;
    private TimerTask contestDataRefresher;
    private BooleanProperty shouldUpdate;
    private boolean isGameOn = false;
    private ContestAndTeamDataAreaController contestAndTeamDataAreaController;

    public ContestDataAreaController() {
        shouldUpdate = new SimpleBooleanProperty();
    }

    public void setContestAndTeamDataAreaController(ContestAndTeamDataAreaController contestAndTeamDataAreaController) {
        this.contestAndTeamDataAreaController = contestAndTeamDataAreaController;
    }

    private void updateSelectedContestData(UboatDTO uboatDTO) {
        Platform.runLater(() -> {
                battleFieldNameLabel.setText(uboatDTO.getBattleName());
                uboatUsernameLabel.setText(uboatDTO.getUboatName());
                gameStatusLabel.setText(uboatDTO.getGameStatus());
                gameLevelLabel.setText(uboatDTO.getLevel());
                activeTeamLabel.setText(uboatDTO.getActiveAlliesAmount() + "/" + uboatDTO.getTotalAlliesAmount());
                if (!isGameOn && uboatDTO.getGameStatus().equals("ACTIVE")) {
                    isGameOn = true;
                    gameOn();
                }
                else if(uboatDTO.getGameStatus().equals("DONE")){
                    contestAndTeamDataAreaController.contestOver();
                }
        });
    }

    public void gameOn() {
        String finalUrl = HttpUrl
                .parse(GET_ENIGMA_MACHINE)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("Fail on Agent: get enigma machine");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBody = response.body().string();
                if (response.code() == 200) {
                    Platform.runLater(() -> contestAndTeamDataAreaController.gameOn(responseBody));
                } else {
                    System.out.println("Something went wrong!!!");
                }
            }
        });
    }

    public void startContestDataAreaRefresher() {
        shouldUpdate.set(true);
        contestDataRefresher = new ContestDataAreaRefresher(this::updateSelectedContestData
        ,shouldUpdate);
        timer = new Timer();
        timer.schedule(contestDataRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    public void resetComponent() {
        battleFieldNameLabel.setText("");
        uboatUsernameLabel.setText("");
        gameStatusLabel.setText("");
        gameLevelLabel.setText("");
        activeTeamLabel.setText("");
        isGameOn = false;
    }
}
