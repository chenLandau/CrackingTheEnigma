package Allies;

import Engine.Engine;
import MyExceptions.ContestIsFullException;
import common.utils.ServletUtils;
import common.utils.SessionUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import usersManager.UsersManager;
import Engine.EngineManager;
import java.io.IOException;

import static common.constants.Constants.*;


@WebServlet(name = "addAlliesToContest", urlPatterns = "/addAlliesToUboatContest") ///////////??
public class AddAllyToUboatContestServlet extends HttpServlet {
    public AddAllyToUboatContestServlet(){}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        String usernameFromSession = SessionUtils.getUsername(request);
        EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());
        String battleFieldName = request.getParameter(BATTLE_FIELD_NAME);
        String allyMissionSize = request.getParameter(ALLY_MISSION_SIZE);

        try {
            engineManager.addAllyToUboat(battleFieldName, usernameFromSession, Integer.parseInt(allyMissionSize));
            engineManager.addAllyToDecryptionManagerMap(usernameFromSession,Integer.parseInt(allyMissionSize), battleFieldName);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (ContestIsFullException e){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
