package Uboat.servlets;

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

@WebServlet(name = "AlliesTeamsListForUboatClient", urlPatterns = "/AlliesTeamsListForUboatClient") ///////////
public class GetAlliesTeamsListServlet extends HttpServlet {
    public GetAlliesTeamsListServlet(){}
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());
        String userNameFromSession = SessionUtils.getUsername(request);
        String userTypeFromSession = SessionUtils.getUserType(request);
        Gson gson = new Gson();

        synchronized (this) {
            try (PrintWriter out = response.getWriter()) {
                Set<AllyTeamDTO> alliesTeamsList = engineManager.getAlliesTeamsListDTO(userTypeFromSession, userNameFromSession);
                if(!alliesTeamsList.isEmpty()) {
                    String json = gson.toJson(alliesTeamsList);
                    out.println(json);
                    out.flush();
                    response.setStatus(HttpServletResponse.SC_OK);
                }else{
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                }
            }
        }
    }
}
