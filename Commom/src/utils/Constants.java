package utils;

import com.google.gson.Gson;

public class Constants {
    public final static String BASE_DOMAIN = "localhost";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/EnigmaWebApp_Web";
    public final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;
    public final static String LOGIN_PAGE = FULL_SERVER_PATH + "/loginShortResponse";
    public final static String SET_USER_READY = FULL_SERVER_PATH + "/setUserReadyResponse";
    public final static String CHECK_IF_ALL_USERS_IS_READY = FULL_SERVER_PATH + "/CheckIfAllUsersIsReady";
    public final static String GET_SELECTED_CONTEST_DATA = FULL_SERVER_PATH + "/getSelectedContestData";

    public final static Gson GSON_INSTANCE = new Gson();
    public final static int REFRESH_RATE = 2000;

}
