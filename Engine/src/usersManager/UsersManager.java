package usersManager;

import DataTransferObject.AgentDTO;
import DataTransferObject.AgentLoginDTO;
import BruteForce.Battlefield.entities.Agent;
import BruteForce.Battlefield.entities.Allies;
import BruteForce.Battlefield.entities.Uboat;
import DataTransferObject.AgentProgressDTO;

import java.util.*;

public class UsersManager {
    private final Map<String, Uboat> userNameToUboatMap;
    private final Map<String, Allies> userNameToAlliesMap;
    private final Map<String, Agent> userNameToAgentMap;

    public UsersManager() {
        userNameToAlliesMap = new HashMap<>();
        userNameToUboatMap = new HashMap<>();
        userNameToAgentMap = new HashMap<>();
    }

    public synchronized void addUboatToMap(Uboat uboat) {
        userNameToUboatMap.put(uboat.getUboatName(), uboat);
    }
    public synchronized void addAlliesToMap(Allies allies) {userNameToAlliesMap.put(allies.getAlliesUserName(), allies);}
    public synchronized void addAgentToMap(Agent agent) {
        userNameToAgentMap.put(agent.getAgentUserName(), agent);
    }
    public synchronized void removeUboat(String username) {
        userNameToUboatMap.remove(username);
    }

    public synchronized void removeAllies(String username) {
        userNameToAlliesMap.remove(username);
    }
    public synchronized void removeAgent(String username) {
        userNameToAgentMap.remove(username);
    }

    public synchronized Allies getAllies(String username){return userNameToAlliesMap.get(username);}
    public synchronized Agent getAgent(String username){return userNameToAgentMap.get(username);}
    public synchronized Map<String, Allies> getAlliesMap() {
        return Collections.unmodifiableMap(userNameToAlliesMap);
    }
    public synchronized Map<String, Agent> getAgentsMap() {
        return userNameToAgentMap;
    }
    public boolean isUserExistsInUboat(String username) {
        return userNameToUboatMap.containsKey(username);
    }

    public boolean isUserExistsInAllies(String username) {
        return userNameToAlliesMap.containsKey(username);
    }

    public boolean isUserExistsInAgents(String username) {
        return userNameToAgentMap.containsKey(username);
    }

    public boolean isUserExists(String username) {
        return isUserExistsInUboat(username) || isUserExistsInAllies(username) || isUserExistsInAgents(username);
    }

//    public synchronized void addAgentToAllyTeam(AgentLoginDTO agentLoginDTO, String agentUserName, boolean isGameActive) {
//        Agent agent = userNameToAgentMap.get(agentUserName);
//        agent.setAgentDetails(agentLoginDTO.getAllyName(),
//                agentLoginDTO.getThreadsAmount(), agentLoginDTO.getMissionsAmount());
//        Allies ally = userNameToAlliesMap.get(agentLoginDTO.getAllyName());
//        ally.addAgentToAllyTeam(agent,isGameActive);
//    }

    public synchronized void setUserReady(Boolean isReady, String userType, String username) {
        switch (userType) {
            case "Uboat":
                userNameToUboatMap.get(username).setUserReady(isReady);
                break;
            case "Allies":
                userNameToAlliesMap.get(username).setUserReady(isReady);
                break;
        }
    }
    public void addUserToMap(String userType, String userName) {
        switch (userType) {
            case "Allies":
                addAlliesToMap(new Allies(userName));
                break;
            case "Agent":
                addAgentToMap(new Agent(userName));
                break;
        }
    }

    public Set<AgentDTO> getActiveAgentsDTOSet(String allyName) {
        Set<AgentDTO> activeAgentsDTOSet = new HashSet<>();
        for (Agent agent : userNameToAlliesMap.get(allyName).getActiveAgents()){
            activeAgentsDTOSet.add(new AgentDTO(agent));
        }
        return activeAgentsDTOSet;
    }

    public void setAgentProgress(String username, AgentProgressDTO agentProgressDTO) {
        userNameToAgentMap.get(username).setAgentProgress(agentProgressDTO);
    }

    public Set<AgentProgressDTO> getAgentsProgressDTOs(String username) {
       return userNameToAlliesMap.get(username).getAgentsProgressDTOs();
    }

    public void setAllyContestOver(String username,boolean isContestOverConfirmed) {
        userNameToAlliesMap.get(username).setContestOverConfirmed(isContestOverConfirmed);
    }

    public boolean isContestOverConfirmed(String allyName) {
        return userNameToAlliesMap.get(allyName).isContestOverConfirmed();
    }

    public void addAgentToAllyTeam(AgentLoginDTO agentLoginDTO, String agentUsername, boolean isGameActive) {
        userNameToAgentMap.get(agentUsername).setAgentDetails(agentLoginDTO.getAllyName(), agentLoginDTO.getThreadsAmount(), agentLoginDTO.getMissionsAmount());
        userNameToAlliesMap.get(agentLoginDTO.getAllyName()).addAgentToAllyTeam(userNameToAgentMap.get(agentUsername),
                isGameActive);
    }
}

