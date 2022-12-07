package BruteForce.Battlefield.entities;

import DataTransferObject.AgentProgressDTO;

public class Agent {
    private String agentUserName;
    private String allyName;
    private int threadsAmount;
    private int missionsAmount;
    private long missionsOnQueue;
    private long totalAmountOfMissionTaken;
    private long missionsCompleted;
    private long totalAmountOfCandidatesFound;

    public Agent(String agentUserName) {
        this.agentUserName = agentUserName;
    }

    public String getAllyName() {
        return allyName;
    }

    public void setAllyName(String allyName) {
        this.allyName = allyName;
    }

    public int getThreadsAmount() {
        return threadsAmount;
    }

    public void setThreadsAmount(int threadsAmount) {
        this.threadsAmount = threadsAmount;
    }

    public int getMissionsAmount() {
        return missionsAmount;
    }

    public void setMissionsAmount(int missionsAmount) {
        this.missionsAmount = missionsAmount;
    }

    public String getAgentUserName() {
        return agentUserName;
    }

    public void setAgentDetails(String allyName, int threadsAmount, int missionsAmount){
        this.allyName = allyName;
        this.threadsAmount = threadsAmount;
        this.missionsAmount = missionsAmount;
    }

    public synchronized void setAgentProgress(AgentProgressDTO agentProgressDTO){
        this.missionsOnQueue = agentProgressDTO.getMissionsOnQueue();
        this.totalAmountOfMissionTaken = agentProgressDTO.getTotalAmountOfMissionTaken();
        this.missionsCompleted = agentProgressDTO.getMissionsCompleted();
        this.totalAmountOfCandidatesFound = agentProgressDTO.getTotalAmountOfCandidatesFound();
    }

    public synchronized AgentProgressDTO getAgentProgress(){
        return new AgentProgressDTO(agentUserName, missionsOnQueue, totalAmountOfMissionTaken,
                missionsCompleted, totalAmountOfCandidatesFound);

    }

}
