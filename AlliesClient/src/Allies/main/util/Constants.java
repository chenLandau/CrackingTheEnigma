package Allies.main.util;

import static utils.Constants.FULL_SERVER_PATH;

public class Constants {

    // fxml locations
    public final static String MAIN_PAGE_FXML_RESOURCE_LOCATION = "/Allies/component/main/allies-app-main.fxml";
    public final static String LOGIN_SCREEN_FXML_RESOURCE_LOCATION = "/Allies/component/LoginScreen/LoginScreen.fxml";
    public final static String DASHBOARD_SCREEN_PAGE_FXML_RESOURCE_LOCATION = "/Allies/component/DashboardScreen/DashboardScreen.fxml";
    public final static String CONTEST_SCREEN_PAGE_FXML_RESOURCE_LOCATION = "/Allies/component/ContestScreen/ContestScreen.fxml";


    // Server resources locations
    public final static String UBOAT_LIST = FULL_SERVER_PATH + "/uboatlist";
    public final static String AGENT_LIST = FULL_SERVER_PATH + "/AgentsTeamList";
    public final static String ADD_ALLIES_TO_UBOAT_CONTEST = FULL_SERVER_PATH + "/addAlliesToUboatContest";
    public final static String GET_ALLIES_TEAMS_LIST = FULL_SERVER_PATH +  "/AlliesTeamsListForAlliesClient";
    public final static String START_TASK_GENERATOR = FULL_SERVER_PATH +  "/startTaskGenerator";
    public final static String GET_NEW_CANDIDATES = FULL_SERVER_PATH +  "/GetNewCandidatesForAllies";
    public final static String GET_AGENT_PROGRESS_LIST = FULL_SERVER_PATH +  "/GetAgentProgressList";
    public final static String CONFIRM_CONTEST_OVER = FULL_SERVER_PATH +  "/ConfirmContestOver";

}
