package Allies;

import DataTransferObject.AllyTeamDTO;
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

@WebServlet(name = "AlliesTeamsListForAlliesClient", urlPatterns = "/AlliesTeamsListForAlliesClient") ///////////
public class GetAlliedTeamsListServlet extends HttpServlet {
    public GetAlliedTeamsListServlet(){}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());
        String userNameFromSession = SessionUtils.getUsername(request);
        String userTypeFromSession = SessionUtils.getUserType(request);

        Gson gson = new Gson();
        synchronized (this) {
            try (PrintWriter out = response.getWriter()) {
                Set<AllyTeamDTO> alliesTeamsList = engineManager.getAlliesTeamsListDTO(userTypeFromSession, userNameFromSession);
                String json = gson.toJson(alliesTeamsList);
                out.println(json);
                out.flush();
            }
        }
    }
}
