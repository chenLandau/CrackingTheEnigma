package Agent.AgentUI.component.ContestScreen;

import Agent.AgentLogic.AgentLogic;
import Agent.AgentUI.component.ContestScreen.AgentCandidates.StringCandidateController;
import Agent.AgentUI.component.ContestScreen.ContestAndTeamDataArea.ContestAndTeamDataAreaController;
import Agent.AgentUI.component.ContestScreen.ContestAndTeamDataArea.ContestDataArea.ContestDataAreaRefresher;
import Agent.AgentUI.main.utils.UIAdapter;
import DataTransferObject.CandidateDTO;
import DataTransferObject.UboatDTO;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleLongProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import utils.Candidates.CandidatesUtils;

import javax.naming.Binding;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static utils.Constants.REFRESH_RATE;

public class ContestScreenController {

    @FXML private StackPane contestAndTeamDataAreaComponent;
    @FXML private ContestAndTeamDataAreaController contestAndTeamDataAreaComponentController;
    @FXML private FlowPane candidatesFlowPane;
    @FXML private Label currentAmountOfMissionOnQueueLabel;
    @FXML private Label totalNumberOfMissionsTakenFromQueueLabel;
    @FXML private Label totalMissionsCompletedLabel;
    @FXML private Label totalCandidatesFoundLabel;
    @FXML private VBox contestOverVbox;
    private SimpleLongProperty currentAmountOfMissionOnQueueProperty;
    private SimpleLongProperty totalNumberOfMissionsTakenFromQueueProperty;
    private SimpleLongProperty totalMissionsCompletedProperty;
    private SimpleLongProperty totalCandidatesFoundProperty;
    private Timer timer;
    private TimerTask isContestOverConfirmRefresher;
    private CandidatesUtils candidatesUtils = new CandidatesUtils();
    private AgentLogic agentLogic;


    @FXML
    public void initialize() {
        contestAndTeamDataAreaComponentController.setContestScreenController(this);
        currentAmountOfMissionOnQueueLabel.textProperty().bind(Bindings.format("%,d",currentAmountOfMissionOnQueueProperty));
        totalNumberOfMissionsTakenFromQueueLabel.textProperty().bind(Bindings.format("%,d",totalNumberOfMissionsTakenFromQueueProperty));
        totalMissionsCompletedLabel.textProperty().bind(Bindings.format("%,d",totalMissionsCompletedProperty));
        totalCandidatesFoundLabel.textProperty().bind(Bindings.format("%,d",totalCandidatesFoundProperty));
    }

    public ContestScreenController() {
        this.currentAmountOfMissionOnQueueProperty = new SimpleLongProperty(0);
        this.totalNumberOfMissionsTakenFromQueueProperty = new SimpleLongProperty(0);
        this.totalMissionsCompletedProperty = new SimpleLongProperty(0);
        this.totalCandidatesFoundProperty = new SimpleLongProperty(0);
    }

    public void setAgentLogic(AgentLogic agentLogic) {
        this.agentLogic = agentLogic;
    }

    public void gameOn(String responseBody) {
        try {
            agentLogic.readCteMachineFromXml(responseBody);
            UIAdapter uiAdapter = createUIAdapter();
            agentLogic.gameOn(uiAdapter);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void setAllyTeamLabel(String allyName){
        contestAndTeamDataAreaComponentController.setAllyTeamLabel(allyName);
    }

    private UIAdapter createUIAdapter(){
        return new UIAdapter(
                candidateDTO -> {
                    candidatesFlowPane.getChildren().add(candidatesUtils.createNewCandidateStringTileForAlly(candidateDTO));
               },
                totalAmountOfMissionTaken -> {
                    this.totalCandidatesFoundProperty.set(totalAmountOfMissionTaken);
                },
                totalAmountOfMissionTaken -> {
                    this.totalNumberOfMissionsTakenFromQueueProperty.set(totalAmountOfMissionTaken);
                },
                totalAmountOfMissionTaken -> {
                    this.currentAmountOfMissionOnQueueProperty.set(totalAmountOfMissionTaken);
                },
                totalAmountOfMissionTaken -> {
                    this.totalMissionsCompletedProperty.set(totalAmountOfMissionTaken);
                }
          );
    }

    public void startContestDataAreaRefresher() {
        contestAndTeamDataAreaComponentController.startContestDataAreaRefresher();
    }
    public void contestOver() {
        contestOverVbox.setVisible(true);
        startContestOverConfirmRefresher();
    }

    public void startContestOverConfirmRefresher() {
        isContestOverConfirmRefresher = new isContestOverConfirmRefresher(this::updateContestOver);
        timer = new Timer();
        timer.schedule(isContestOverConfirmRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    private void updateContestOver(Boolean isContestOverConfirmed) {
        if (isContestOverConfirmed) {
            Platform.runLater(() -> {
                agentLogic.contestOverConfirmed();
                contestOverVbox.setVisible(false);
                contestAndTeamDataAreaComponentController.resetComponent();
                candidatesFlowPane.getChildren().clear();
                currentAmountOfMissionOnQueueProperty.set(0);
                totalNumberOfMissionsTakenFromQueueProperty.set(0);
                totalMissionsCompletedProperty.set(0);
                totalCandidatesFoundProperty.set(0);
                isContestOverConfirmRefresher.cancel();
                startContestDataAreaRefresher();
            });
        }
    }
}

