package Allies;

import DataTransferObject.CandidatesAndVersionDTO;
import DataTransferObject.CandidatesAndVersionForUboatDTO;
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

import static common.constants.Constants.CHAT_VERSION_PARAMETER;
import static common.constants.Constants.INT_PARAMETER_ERROR;

@WebServlet(name = "GetNewCandidatesForAllies", urlPatterns = "/GetNewCandidatesForAllies")
public class GetNewCandidatesForAlliesServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());
        String userNameFromSession = SessionUtils.getUsername(request);
        if (userNameFromSession == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        int candidateVersion = ServletUtils.getIntParameter(request, CHAT_VERSION_PARAMETER);
        if (candidateVersion == INT_PARAMETER_ERROR) {
            return;
        }

        synchronized (this) {
            CandidatesAndVersionDTO candidatesAndVersionDTO = engineManager.getCandidatesAndVersionDTOForAllies(userNameFromSession, candidateVersion);
            Gson gson = new Gson();
            String jsonResponse = gson.toJson(candidatesAndVersionDTO);
            try (PrintWriter out = response.getWriter()) {
                out.print(jsonResponse);
                out.flush();
            }
        }
    }
}