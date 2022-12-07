package DataTransferObject;

public class AgentProgressDTO {
    private String agentName;
    private long missionsOnQueue;
    private long totalAmountOfMissionTaken;
    private long missionsCompleted;
    private long totalAmountOfCandidatesFound;

    public AgentProgressDTO(String agentName, long missionsOnQueue, long totalAmountOfMissionTaken, long missionsCompleted, long totalAmountOfCandidatesFound){
        this.agentName = agentName;
        this.missionsOnQueue = missionsOnQueue;
        this.totalAmountOfMissionTaken = totalAmountOfMissionTaken;
        this.missionsCompleted = missionsCompleted;
        this.totalAmountOfCandidatesFound = totalAmountOfCandidatesFound;
    }

    public String getAgentName() {
        return agentName;
    }

    public long getMissionsOnQueue() {
        return missionsOnQueue;
    }

    public long getTotalAmountOfMissionTaken() {
        return totalAmountOfMissionTaken;
    }

    public long getMissionsCompleted() {
        return missionsCompleted;
    }

    public long getTotalAmountOfCandidatesFound() {
        return totalAmountOfCandidatesFound;
    }
}
