package Uboat.component.ContestScreen.CandidatesArea;

import DataTransferObject.CandidatesAndVersionForUboatDTO;
import Uboat.component.ContestScreen.ContestScreenController;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;
import utils.Candidates.CandidatesUtils;

import java.util.List;
import java.util.Timer;
import java.util.stream.Collectors;

import static utils.Constants.REFRESH_RATE;

public class CandidatesAreaController {
    @FXML private FlowPane candidatesFlowPane;
    private final IntegerProperty candidatesVersion;
    private final BooleanProperty autoUpdate;
    private CandidateAreaRefresher candidateAreaRefresher;
    private Timer timer;
    private ContestScreenController contestScreenController;
    private CandidatesUtils candidatesUtils = new CandidatesUtils();


    public CandidatesAreaController() {
        candidatesVersion = new SimpleIntegerProperty();
        autoUpdate = new SimpleBooleanProperty(true);
    }

    private void updateCandidates(CandidatesAndVersionForUboatDTO candidatesAndVersionDTO) {
        if (candidatesAndVersionDTO.getVersion() != candidatesVersion.get()) {
            List<Node> deltaCandidates = candidatesAndVersionDTO
                    .getCandidates()
                    .stream()
                    .map(candidateDTO -> {
                        return candidatesUtils.createNewCandidateStringTileForUboat(candidateDTO);
                    }).collect(Collectors.toList());
            Platform.runLater(() -> {
                candidatesVersion.set(candidatesAndVersionDTO.getVersion());
                candidatesFlowPane.getChildren().addAll(deltaCandidates);
            });
        }
            if(candidatesAndVersionDTO.isWinnerExist()){
                autoUpdate.set(false);
                showWinnerTeamDetails();
            }
    }

    public void reset(){
        candidatesFlowPane.getChildren().clear();
    }

    private void showWinnerTeamDetails() {
        contestScreenController.showWinnerTeamDetails();
    }

    public void startCandidateAreaRefresher() {
       // candidatesVersion.set(0);
        autoUpdate.set(true);
        candidateAreaRefresher = new CandidateAreaRefresher(
                candidatesVersion,
                autoUpdate,
                this::updateCandidates);
        timer = new Timer();
        timer.schedule(candidateAreaRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    public void setContestScreenController(ContestScreenController contestScreenController) {
        this.contestScreenController = contestScreenController;
    }
}
