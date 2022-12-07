package Agent;

import DataTransferObject.AgentProgressDTO;
import DataTransferObject.CandidateDTO;
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
import java.util.Arrays;


@WebServlet(name = "SetAgentProgress", urlPatterns = "/SetAgentProgress")
public class SetAgentProgressServlet extends HttpServlet {
    public SetAgentProgressServlet() {
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());
        String userNameFromSession = SessionUtils.getUsername(request);

        if (userNameFromSession == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            BufferedReader reader = request.getReader();
            Gson gson = new Gson();
            synchronized (this) {
                AgentProgressDTO agentProgressDTO = gson.fromJson(reader, AgentProgressDTO.class);
                engineManager.setAgentProgress(userNameFromSession, agentProgressDTO);
            }
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
