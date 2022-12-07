package Agent;

import DataTransferObject.UboatDTO;
import Engine.EngineManager;
import MyExceptions.NoContestSelectionException;
import com.google.gson.Gson;
import common.utils.ServletUtils;
import common.utils.SessionUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "getSelectedContestDataForAgent", urlPatterns = "/getSelectedContestDataForAgent")
public class GetSelectedContestDataForAgentServlet extends HttpServlet{
    public GetSelectedContestDataForAgentServlet(){}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());
        String allyNameFromSession = SessionUtils.getAllyName(request);

        if (allyNameFromSession == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            try {
                synchronized (this) {
                    PrintWriter out = response.getWriter();
                    UboatDTO uboatDTO = engineManager.getUboatDTOForAgent(allyNameFromSession);
                    Gson gson = new Gson();
                    String json = gson.toJson(uboatDTO);
                    out.println(json);
                    out.flush();
                    response.setStatus(HttpServletResponse.SC_OK);
                }
            } catch (NoContestSelectionException e) {
                response.setStatus(HttpServletResponse.SC_OK);
            }
        }
    }
}
