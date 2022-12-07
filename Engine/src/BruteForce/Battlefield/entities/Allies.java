package BruteForce.Battlefield.entities;
import DataTransferObject.AgentProgressDTO;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class Allies {
    private String alliesUserName;
    private int missionSize;
    private Set<Agent> activeAgents = new LinkedHashSet<>();
    private Set<Agent> waitingAgents = new HashSet<>();
    private boolean isUserReady = false;
    private boolean isContestOverConfirmed = false;
    public Allies(String alliesUserName) {
        this.alliesUserName = alliesUserName;
    }
    public String getAlliesUserName() {
        return alliesUserName;
    }
    public boolean isUserReady() {
        return isUserReady;
    }
    public void setUserReady(boolean userReady) {
        isUserReady = userReady;
    }
    public int getAgentsAmount() { return activeAgents.size(); }
    public int getMissionSize() { return missionSize; }
    public void setMissionSize(int missionSize) { this.missionSize = missionSize; }

    public synchronized void addAgentToAllyTeam(Agent agent, boolean isGameActive) {
        if(isGameActive){
            waitingAgents.add(agent);
        }
        else {
            activeAgents.add(agent);
        }
    }
    public Set<Agent> getActiveAgents() {
        return activeAgents;
    }

    public boolean isContestOverConfirmed() {
        return isContestOverConfirmed;
    }

    public void setContestOverConfirmed(boolean contestOverConfirmed) {
        isContestOverConfirmed = contestOverConfirmed;
    }

    public synchronized Set<AgentProgressDTO> getAgentsProgressDTOs() {
        Set<AgentProgressDTO> agentProgressDTOS =  new LinkedHashSet<>();
        for(Agent agent : activeAgents){
            agentProgressDTOS.add(agent.getAgentProgress());
        }
        return agentProgressDTOS;
    }

    public synchronized void agentUnion() {
        activeAgents.addAll(waitingAgents);
        waitingAgents.clear();
    }
}
