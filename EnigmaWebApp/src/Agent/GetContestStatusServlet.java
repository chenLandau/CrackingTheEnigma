package Agent;

import DataTransferObject.ContestStatusDTO;
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

@WebServlet(name = "GetContestStatus", urlPatterns = "/GetContestStatus")
public class GetContestStatusServlet extends HttpServlet {
    public GetContestStatusServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        String allyNameFromSession = SessionUtils.getAllyName(request);
        EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());
        Gson gson = new Gson();

        if(allyNameFromSession == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }else {
            synchronized (this) {
                ContestStatusDTO contestStatusDTO = engineManager.getContestStatusDTO(allyNameFromSession);
                PrintWriter out = response.getWriter();
                String json = gson.toJson(contestStatusDTO);
                out.println(json);
                out.flush();
                response.setStatus(HttpServletResponse.SC_OK);
            }
        }
    }
}
