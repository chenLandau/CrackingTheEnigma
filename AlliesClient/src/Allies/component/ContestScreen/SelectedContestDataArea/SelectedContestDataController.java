package Allies.component.ContestScreen.SelectedContestDataArea;

import Allies.component.ContestScreen.ContestScreenController;
import Allies.component.main.AlliesMainController;
import DataTransferObject.UboatDTO;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.util.Timer;
import java.util.TimerTask;

import static utils.Constants.REFRESH_RATE;


public class SelectedContestDataController {
    @FXML private Label battleFieldNameLabel;
    @FXML private Label uboatUsernameLabel;
    @FXML private Label gameStatusLabel;
    @FXML private Label gameLevelLabel;
    @FXML private Label activeTeamLabel;
    @FXML private Label noContestLabel;
    @FXML private HBox contestDataHBox;
    private Timer timer;
    private TimerTask selectedContestDataRefresher;
    private final BooleanProperty shouldUpdate;
    private ContestScreenController contestScreenController;
    private boolean isGameOn = false;


    public SelectedContestDataController() {
        shouldUpdate = new SimpleBooleanProperty(false);
    }

    public BooleanProperty autoUpdatesProperty() {
        return shouldUpdate;
    }
    public void setContestScreenController(ContestScreenController contestScreenController) {
        this.contestScreenController = contestScreenController;
    }

    private void updateSelectedContestData(UboatDTO uboatDTO) {
        Platform.runLater(() -> {
            battleFieldNameLabel.setText(uboatDTO.getBattleName());
            uboatUsernameLabel.setText(uboatDTO.getUboatName());
            gameStatusLabel.setText(uboatDTO.getGameStatus());
            gameLevelLabel.setText(uboatDTO.getLevel());
            activeTeamLabel.setText(uboatDTO.getActiveAlliesAmount() + "/" + uboatDTO.getTotalAlliesAmount());
        });
        if (!isGameOn && uboatDTO.getGameStatus().equals("ACTIVE")) {
            isGameOn = true;
            contestScreenController.gameOn();
        }
        else if(uboatDTO.getGameStatus().equals("DONE")){
            selectedContestDataRefresher.cancel();
            contestScreenController.contestOver();
        }
    }

    public void SelectedContestDataRefresher() {
        noContestLabel.setVisible(false);
        contestDataHBox.setVisible(true);
        shouldUpdate.set(true);
        selectedContestDataRefresher = new SelectedContestDataRefresher(this::updateSelectedContestData,
                shouldUpdate);
        timer = new Timer();
        timer.schedule(selectedContestDataRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    public void resetComponent() {
        noContestLabel.setVisible(true);
        contestDataHBox.setVisible(false);
       battleFieldNameLabel.setText("");
       uboatUsernameLabel.setText("");
       gameStatusLabel.setText("");
       gameLevelLabel.setText("");
       activeTeamLabel.setText("");
       shouldUpdate.set(false);
       isGameOn = false;
    }
}