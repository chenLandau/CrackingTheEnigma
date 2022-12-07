package Uboat.servlets;

import DataTransferObject.CandidateDTO;
import DataTransferObject.MachineSpecificationsDTO;
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


@WebServlet(name = "GetWinnerTeamDetails", urlPatterns = "/GetWinnerTeamDetails")
public class GetWinnerTeamDetailsServlet extends HttpServlet {
    public GetWinnerTeamDetailsServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());
            String userNameFromSession = SessionUtils.getUsername(request);
            synchronized (this) {
                CandidateDTO candidateDTO = engineManager.getWinnerTeamDetails(userNameFromSession);
                String json = gson.toJson(candidateDTO);
                out.println(json);
                out.flush();
            }
        }
    }
}
