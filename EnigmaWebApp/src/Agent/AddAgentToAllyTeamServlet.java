package Agent;

import DataTransferObject.AgentLoginDTO;
import Engine.EngineManager;
import com.google.gson.Gson;
import common.utils.ServletUtils;
import common.utils.SessionUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

import static common.constants.Constants.ALLY_TEAM_NAME;


@WebServlet(name = "addAgentToAllyTeam", urlPatterns = "/addAgentToAllyTeam")
public class AddAgentToAllyTeamServlet extends HttpServlet {
    public AddAgentToAllyTeamServlet() {
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //response.setContentType("application/json");
        String usernameFromSession = SessionUtils.getUsername(request);
        if(usernameFromSession == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }else{
                String allyNameFromSession = SessionUtils.getAllyName(request);
                EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());
                BufferedReader reader = request.getReader();
                Gson gson = new Gson();

                if (allyNameFromSession == null) {
                    AgentLoginDTO agentLoginDTO = gson.fromJson(reader, AgentLoginDTO.class);
                    engineManager.addAgentToAllyTeam(agentLoginDTO, usernameFromSession);
                    request.getSession(false).setAttribute(ALLY_TEAM_NAME, agentLoginDTO.getAllyName());
                    response.setStatus(HttpServletResponse.SC_OK);
                }else{
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                }
            }
        }
}