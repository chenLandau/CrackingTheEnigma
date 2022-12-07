package Agent.AgentUI.main.utils;

import DataTransferObject.CandidateDTO;
import javafx.application.Platform;

import java.util.function.Consumer;

public class UIAdapter {
    private Consumer<CandidateDTO> introduceDecryptOptionalString;
    private Consumer<Long> currentAmountOfMissionOnQueueDelegate;
    private Consumer<Long> totalCandidatesFoundDelegate;
    private Consumer<Long> totalNumberOfMissionsTakenFromQueueDelegate;
    private Consumer<Long> totalMissionsCompletedDelegate;

    public UIAdapter(Consumer<CandidateDTO> introduceDecryptOptionalString, Consumer<Long> totalCandidatesFoundDelegate,
                     Consumer<Long> totalNumberOfMissionsTakenFromQueueDelegate,
                     Consumer<Long> currentAmountOfMissionOnQueueDelegate,Consumer<Long> totalMissionsCompletedDelegate){
        this.introduceDecryptOptionalString = introduceDecryptOptionalString;
        this.totalCandidatesFoundDelegate = totalCandidatesFoundDelegate;
        this.totalNumberOfMissionsTakenFromQueueDelegate = totalNumberOfMissionsTakenFromQueueDelegate;
        this.currentAmountOfMissionOnQueueDelegate = currentAmountOfMissionOnQueueDelegate;
        this.totalMissionsCompletedDelegate = totalMissionsCompletedDelegate;
    }

    public void addNewDecryptOptionalString(CandidateDTO decodedStringDTO){
        Platform.runLater(() -> {
                    this.introduceDecryptOptionalString.accept(decodedStringDTO);
                });
    }
    public void setCurrentAmountOfMissionOnQueueDelegate(long currentAmountOfMission){
         Platform.runLater(() -> {
            this.currentAmountOfMissionOnQueueDelegate.accept(currentAmountOfMission);
        });
    }
    public void setNumberOfCandidateFoundString(long totalCandidateFound){
        Platform.runLater(() -> {
            this.totalCandidatesFoundDelegate.accept(totalCandidateFound);
        });
    }

    public void setTotalNumberOfMissionsTakenFromQueue(long totalNumberOfMissionsTaken){
        Platform.runLater(() -> {
            this.totalNumberOfMissionsTakenFromQueueDelegate.accept(totalNumberOfMissionsTaken);
        });
    }

    public void setTotalMissionsCompletedDelegate(long totalMissionsCompleted){
        Platform.runLater(() -> {
            this.totalMissionsCompletedDelegate.accept(totalMissionsCompleted);
        });
    }

}
