package DataTransferObject;

import BruteForce.Battlefield.entities.Agent;

public class AgentDTO {

    private String agentsName;
    private int threadsAmount;
    private int missionsAmount;

    public AgentDTO(Agent agent) {
        this.agentsName = agent.getAgentUserName();
        this.threadsAmount = agent.getThreadsAmount();
        this.missionsAmount = agent.getMissionsAmount();
    }

    public String getAgentsName() {
        return agentsName;
    }

    public int getThreadsAmount() {
        return threadsAmount;
    }

    public int getMissionsAmount() {
        return missionsAmount;
    }
}
