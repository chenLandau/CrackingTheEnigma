package Agent.AgentLogic;

import Agent.AgentUI.main.utils.UIAdapter;
import javafx.application.Platform;

import java.util.function.Consumer;

public class AgentMissionCounter {
    private long missionsOnQueue;
    private long missionsCompleted;
    private long totalCandidatesFound = 0;
    private long totalAmountOfMissionTaken = 0;
    private UIAdapter uiAdapter;

    public AgentMissionCounter() {}

    public void setUiAdapter(UIAdapter uiAdapter) {
        this.uiAdapter = uiAdapter;
    }
    public synchronized void setMissionsCounter(int missionsCounter) {
        this.missionsOnQueue = missionsCounter;
    }

    public synchronized void addMissionsToTotalAmountOfMissionTaken(long missionsTaken){totalAmountOfMissionTaken += missionsTaken;}

    public synchronized long getMissionsOnQueue() {
        return missionsOnQueue;
    }

    public synchronized long getTotalAmountOfMissionTaken() {return totalAmountOfMissionTaken;}

    public synchronized long getMissionsCompleted() {
        return missionsCompleted;
    }

    public synchronized long getTotalCandidatesFound() {
        return totalCandidatesFound;
    }

    public synchronized void missionDone(long totalCandidatesFound) {
        this.missionsOnQueue--;
        this.missionsCompleted++;
        this.totalCandidatesFound += totalCandidatesFound;
    }

    public synchronized void contestOverConfirmed() {
        this.totalCandidatesFound = 0;
        this.totalAmountOfMissionTaken = 0;
        this.missionsOnQueue = 0;
        this.missionsCompleted = 0;
    }
}

