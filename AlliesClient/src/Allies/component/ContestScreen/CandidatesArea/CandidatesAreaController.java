package Allies.component.ContestScreen.CandidatesArea;

import DataTransferObject.CandidatesAndVersionForUboatDTO;
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
    private CandidatesUtils candidatesUtils = new CandidatesUtils();

    public CandidatesAreaController() {
        candidatesVersion = new SimpleIntegerProperty();
        autoUpdate = new SimpleBooleanProperty();
    }

    private void updateCandidates(CandidatesAndVersionForUboatDTO candidatesAndVersionDTO) {
        if (candidatesAndVersionDTO.getVersion() != candidatesVersion.get()) {
            List<Node> deltaCandidates = candidatesAndVersionDTO
                    .getCandidates()
                    .stream()
                    .map(candidateDTO -> {
                        return candidatesUtils.createNewCandidateStringTileForAlly(candidateDTO);
                    }).collect(Collectors.toList());

            Platform.runLater(() -> {
                candidatesVersion.set(candidatesAndVersionDTO.getVersion());
                candidatesFlowPane.getChildren().addAll(deltaCandidates);
            });
        }
    }

    public void startCandidateAreaRefresher() {
        autoUpdate.set(true);
       // candidatesVersion.set(0);
        candidateAreaRefresher = new CandidateAreaRefresher(
                candidatesVersion,
                autoUpdate,
                this::updateCandidates);
        timer = new Timer();
        timer.schedule(candidateAreaRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    public void resetComponent() {
        candidatesFlowPane.getChildren().clear();
        autoUpdate.set(false);
    }
}
