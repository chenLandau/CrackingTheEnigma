package DataTransferObject;

import BruteForce.Battlefield.entities.Allies;

public class AllyTeamDTO {
    private String teamName;
    private int agentsAmount;
    private int missionSize;

    public AllyTeamDTO(String teamName, int agentsAmount, int missionSize) {
        this.teamName = teamName;
        this.agentsAmount = agentsAmount;
        this.missionSize = missionSize;
    }
    public AllyTeamDTO(Allies allies) {
        this.teamName = allies.getAlliesUserName();
        this.agentsAmount = allies.getAgentsAmount();
        this.missionSize = allies.getMissionSize();
    }

    public String getAlliesTeamName() { return teamName; }
    public int getAgentsAmount() {
        return agentsAmount;
    }
    public int getMissionSize() { return missionSize; }

}
