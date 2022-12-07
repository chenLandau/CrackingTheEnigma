package Uboat.main.utils;

import static utils.Constants.FULL_SERVER_PATH;

public class Constants {

    // fxml locations
    public final static String MAIN_PAGE_FXML_RESOURCE_LOCATION = "/Uboat/component/main/uboat-app-main.fxml";
    public final static String LOGIN_SCREEN_FXML_RESOURCE_LOCATION = "/Uboat/component/LoginScreen/LoginScreen.fxml";
    public final static String MACHINE_SCREEN_PAGE_FXML_RESOURCE_LOCATION = "/Uboat/component/MachineScreen/MachineScreen.fxml";
    public final static String CONTEST_SCREEN_PAGE_FXML_RESOURCE_LOCATION = "/Uboat/component/ContestScreen/ContestScreen.fxml";

    // Server resources locations

    public final static String FILE_UPLOAD_PAGE = FULL_SERVER_PATH + "/FileUploadResponse";
    public final static String MACHINE_DETAILS_PAGE = FULL_SERVER_PATH + "/MachineDetailsResponse";

    public final static String CODE_CONFIGURATION_DETAILS_PAGE = FULL_SERVER_PATH + "/CodeConfigurationDetailsResponse";

    public final static String INITIALIZE_CODE_CONFIGURATION_PAGE = FULL_SERVER_PATH + "/InitializeCodeConfigurationResponse";

    public final static String CODE_CONFIGURATION_PAGE = FULL_SERVER_PATH + "/CodeConfigurationResponse";
    public final static String DICTIONARY_WORDS_PAGE = FULL_SERVER_PATH + "/dictionaryWordsResponse";
    public static final String ENCRYPT_STRING = FULL_SERVER_PATH + "/encryptString";
    public static final String RESET_MACHINE = FULL_SERVER_PATH + "/resetMachine";

    public final static String GET_ALLIES_TEAMS_LIST = FULL_SERVER_PATH +  "/AlliesTeamsListForUboatClient";

    public final static String GET_NEW_CANDIDATES = FULL_SERVER_PATH +  "/GetNewCandidatesForUboat";
    public final static String GET_WINNER_TEAM_DETAILS = FULL_SERVER_PATH +  "/GetWinnerTeamDetails";
    public final static String RESET_CURRENT_CONTEST = FULL_SERVER_PATH +  "/ResetCurrentContest";
    public final static String LOG_OUT = FULL_SERVER_PATH +  "/Logout";




}
