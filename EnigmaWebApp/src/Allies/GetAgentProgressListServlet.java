package Allies;

import DataTransferObject.AgentProgressDTO;
import Engine.EngineManager;
import com.google.gson.Gson;
import common.utils.ServletUtils;
import common.utils.SessionUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

@WebServlet(name = "AgentsProgressList", urlPatterns = "/GetAgentProgressList")
public class GetAgentProgressListServlet extends HttpServlet {
    public GetAgentProgressListServlet() {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        String usernameFromSession = SessionUtils.getUsername(request);
        EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());
        Gson gson = new Gson();

        synchronized (this) {
            Set<AgentProgressDTO> agentProgressDTOSet = engineManager.getAgentsProgressDTOs(usernameFromSession);
            PrintWriter out = response.getWriter();
            String json = gson.toJson(agentProgressDTOSet);
            out.println(json);
            out.flush();
        }
    }
}


