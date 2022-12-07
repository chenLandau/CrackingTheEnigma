package Agent.AgentUI.main.utils;

import static utils.Constants.FULL_SERVER_PATH;

public class Constants {
    public final static String MAIN_PAGE_FXML_RESOURCE_LOCATION = "/Agent/AgentUI/component/main/agent-app-main.fxml";
    public final static String LOGIN_SCREEN_FXML_RESOURCE_LOCATION = "/Agent/AgentUI/component/LoginScreen/LoginScreen.fxml";
    public final static String LOAD_AGENT_DETAILS_SCREEN_FXML_RESOURCE_LOCATION = "/Agent/AgentUI/component/LoadAgentDetailsScreen/LoadAgentDetailsScreen.fxml";
    public final static String CONTEST_SCREEN_FXML_RESOURCE_LOCATION = "/Agent/AgentUI/component/ContestScreen/ContestScreen.fxml";


    public final static String GET_ALLIES_TEAMS_LIST = FULL_SERVER_PATH + "/AlliesTeamsListForAgentClient";
    public final static String ADD_AGENT_TO_ALLY_TEAM = FULL_SERVER_PATH + "/addAgentToAllyTeam";
    public final static String GET_ENIGMA_MACHINE = FULL_SERVER_PATH + "/GetEnigmaMachine";
    public final static String GET_AGENT_MISSIONS_LIST = FULL_SERVER_PATH + "/GetAgentMissionsList";
    public final static String ADD_NEW_CANDIDATES = FULL_SERVER_PATH + "/AddNewCandidates";
    public final static String GET_CONTEST_LIST_SERVLET_FOR_AGENT = FULL_SERVER_PATH + "/getSelectedContestDataForAgent";
    public final static String GET_CONTEST_STATUS_SERVLET = FULL_SERVER_PATH + "/GetContestStatus";
    public final static String SET_AGENT_PROGRESS = FULL_SERVER_PATH + "/SetAgentProgress";
    public final static String IS_CONTEST_OVER_CONFIRMED = FULL_SERVER_PATH + "/GetIsContestOverConfirmed";


}
