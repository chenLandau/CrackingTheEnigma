package common.servlets;


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


@WebServlet(name = "CheckIfAllUsersIsReady", urlPatterns = "/CheckIfAllUsersIsReady") ///////////
public class CheckIfAllUsersIsReadyServlet extends HttpServlet {
    public CheckIfAllUsersIsReadyServlet(){}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());
        Boolean isGameStatusActive = false;
        String userNameFromSession = SessionUtils.getUsername(request);
        String userTypeFromSession = SessionUtils.getUserType(request);

        synchronized (this){
            PrintWriter out = response.getWriter();
            Gson gson = new Gson();
            isGameStatusActive = engineManager.checkIfGameStatusActive(userTypeFromSession,userNameFromSession);
            String json = gson.toJson(isGameStatusActive);
            out.println(json);
            out.flush();
        }
    }
}
