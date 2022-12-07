package Allies;

import DataTransferObject.AgentDTO;
import DataTransferObject.AllyTeamDTO;
import Engine.EngineManager;
import com.google.gson.Gson;
import common.utils.ServletUtils;
import common.utils.SessionUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import usersManager.UsersManager;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

@WebServlet(name = "AgentsTeamList", urlPatterns = "/AgentsTeamList")
public class GetAgentsTeamListServlet extends HttpServlet {

    public GetAgentsTeamListServlet(){}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        String usernameFromSession = SessionUtils.getUsername(request);
        EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());
        Gson gson = new Gson();

        synchronized (this) {
            Set<AgentDTO> activeAgentsDTOSet = engineManager.getActiveAgentsDTOSet(usernameFromSession);
            try (PrintWriter out = response.getWriter()) {
                String json = gson.toJson(activeAgentsDTOSet);
                out.println(json);
                out.flush();
            }
        }
    }
}