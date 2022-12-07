package Allies.component.ContestScreen.AgentsProgressData;

import DataTransferObject.AgentProgressDTO;
import javafx.beans.property.SimpleStringProperty;

public class AgentsProgressString {
    private SimpleStringProperty agentName;
    private SimpleStringProperty missionsOnQueue;
    private SimpleStringProperty totalAmountOfMissionTaken;
    private SimpleStringProperty missionsCompleted;
    private SimpleStringProperty totalAmountOfCandidatesFound;

    public AgentsProgressString(AgentProgressDTO agentProgressDTO) {
        this.agentName = new SimpleStringProperty(agentProgressDTO.getAgentName());
        this.missionsOnQueue = new SimpleStringProperty(String.valueOf(agentProgressDTO.getMissionsOnQueue()));
        this.totalAmountOfMissionTaken = new SimpleStringProperty(String.valueOf(agentProgressDTO.getTotalAmountOfMissionTaken()));
        this.missionsCompleted = new SimpleStringProperty(String.valueOf(agentProgressDTO.getMissionsCompleted()));
        this.totalAmountOfCandidatesFound = new SimpleStringProperty(String.valueOf(agentProgressDTO.getTotalAmountOfCandidatesFound()));
    }

    public String getAgentName() { return agentName.get(); }

    public void setAgentName(String AgentName) { this.agentName.set(AgentName); }

    public String getMissionsOnQueue() {return missionsOnQueue.get();}

    public void setMissionsOnQueue(String missionsOnQueue) {this.missionsOnQueue.set(missionsOnQueue);}

    public String getTotalAmountOfMissionTaken() {return totalAmountOfMissionTaken.get();}

    public void setTotalAmountOfMissionTaken(String totalAmountOfMissionTaken) {
        this.totalAmountOfMissionTaken.set(totalAmountOfMissionTaken);
    }
    public String getMissionsCompleted() {
        return missionsCompleted.get();
    }

    public void setMissionsCompleted(String missionsCompleted) {
        this.missionsCompleted.set(missionsCompleted);
    }
    public String getTotalAmountOfCandidatesFound() {
        return totalAmountOfCandidatesFound.get();
    }

    public void setTotalAmountOfCandidatesFound(String totalAmountOfCandidatesFound) {
        this.totalAmountOfCandidatesFound.set(totalAmountOfCandidatesFound);
    }
}
