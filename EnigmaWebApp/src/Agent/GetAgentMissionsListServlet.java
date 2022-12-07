package Agent;

import DataTransferObject.AgentMissionDTO;
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
import java.util.List;


@WebServlet(name = "GetAgentMissionsList", urlPatterns = "/GetAgentMissionsList")
public class GetAgentMissionsListServlet extends HttpServlet {
    public GetAgentMissionsListServlet() {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        String usernameFromSession = SessionUtils.getUsername(request);
        String allyNameFromSession = SessionUtils.getAllyName(request);
        if(usernameFromSession == null || allyNameFromSession == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }else {
            EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());
            Gson gson = new Gson();
            synchronized (this) {
                List<AgentMissionDTO> agentMissionsDTO = engineManager.getAgentMissionsList(usernameFromSession, allyNameFromSession);
                PrintWriter out = response.getWriter();
                String json = gson.toJson(agentMissionsDTO);
                out.println(json);
                out.flush();
                response.setStatus(HttpServletResponse.SC_OK);
            }
        }
    }
}
