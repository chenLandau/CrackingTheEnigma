package Uboat.servlets;

import Engine.EngineManager;
import common.utils.ServletUtils;
import common.utils.SessionUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet(name = "ResetCurrentContest", urlPatterns = "/ResetCurrentContest")
public class ResetCurrentContestServlet extends HttpServlet {
    public ResetCurrentContestServlet() {
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());
        String usernameFromSession = SessionUtils.getUsername(request);

        synchronized (this) {
            try {
                engineManager.ResetCurrentContest(usernameFromSession);
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                e.printStackTrace();
            }
        }
    }
}
