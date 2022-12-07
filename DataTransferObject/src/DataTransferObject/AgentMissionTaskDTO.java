package DataTransferObject;

import java.util.List;

public class AgentMissionTaskDTO {
        private List<AgentMissionDTO> agentMissionList;
        private boolean isTaskGeneratorCompleted;


    public AgentMissionTaskDTO(List<AgentMissionDTO> agentMissionList,boolean isTaskGeneratorCompleted){
        this.isTaskGeneratorCompleted = isTaskGeneratorCompleted;
        this.agentMissionList = agentMissionList;
    }

    public boolean isTaskGeneratorCompleted() {
        return isTaskGeneratorCompleted;
    }

    public List<AgentMissionDTO> getAgentMissionList() {
        return agentMissionList;
    }
}
