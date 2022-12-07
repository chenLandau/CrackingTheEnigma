package Allies.component.ContestScreen.AlliesTeamsDataArea;

import DataTransferObject.AllyTeamDTO;
import javafx.beans.property.SimpleStringProperty;

public class AlliesTeamsDataString {
    private SimpleStringProperty teamName;
    private SimpleStringProperty agentsAmount;
    private SimpleStringProperty missionSize;

    public AlliesTeamsDataString(AllyTeamDTO alliesTeamDTO) {
        this.teamName = new SimpleStringProperty(alliesTeamDTO.getAlliesTeamName());
        this.agentsAmount = new SimpleStringProperty(String.valueOf(alliesTeamDTO.getAgentsAmount()));
        this.missionSize = new SimpleStringProperty(String.valueOf(alliesTeamDTO.getMissionSize()));
    }

    public String getTeamName() { return teamName.get(); }

    public void setTeamName(String teamName) { this.teamName.set(teamName); }

    public String getAgentsAmount() { return agentsAmount.get(); }

    public void setAgentsAmount(String agentsAmount) { this.agentsAmount.set(agentsAmount); }

    public String getMissionSize() { return missionSize.get(); }

    public void setMissionSize(String missionSize) { this.missionSize.set(missionSize); }

}
