package Allies.component.DashboardScreen.AgentsDataArea;

import DataTransferObject.AgentDTO;
import javafx.beans.property.SimpleStringProperty;


public class AgentString {
        private SimpleStringProperty agentsName;
        private SimpleStringProperty threadsAmount;
        private SimpleStringProperty missionsAmount;
    public AgentString(AgentDTO agent) {
        this.agentsName = new SimpleStringProperty(agent.getAgentsName());
        this.threadsAmount = new SimpleStringProperty(String.valueOf(agent.getThreadsAmount()));
        this.missionsAmount = new SimpleStringProperty(String.valueOf(agent.getMissionsAmount()));
    }

    public String getAgentsName() { return agentsName.get(); }

    public void setAgentsName(String agentsName) { this.agentsName.set(agentsName); }

    public String getMissionsAmount() { return missionsAmount.get(); }

    public void setMissionsAmount(String missionsAmount) { this.missionsAmount.set(missionsAmount); }

    public String getThreadsAmount() { return threadsAmount.get(); }

    public void setThreadsAmount(String threadsAmount) { this.threadsAmount.set(threadsAmount); }
}
