package Allies;

import Engine.EngineManager;
import common.utils.ServletUtils;
import common.utils.SessionUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "ConfirmContestOver", urlPatterns = "/ConfirmContestOver")
public class ConfirmContestOverServlet extends HttpServlet {
    public ConfirmContestOverServlet() {
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());
        String usernameFromSession = SessionUtils.getUsername(request);

        synchronized (this) {
            try {
                engineManager.setAllyContestOver(usernameFromSession);
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                e.printStackTrace();
            }
        }
    }
}
