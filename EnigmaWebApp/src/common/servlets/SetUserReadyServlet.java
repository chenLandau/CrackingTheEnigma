package common.servlets;

import Engine.EngineManager;
import common.utils.ServletUtils;
import common.utils.SessionUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import static common.constants.Constants.USER_TYPE;

@WebServlet(name = "SetUserReady", urlPatterns = "/setUserReadyResponse")
public class SetUserReadyServlet extends HttpServlet {
    public SetUserReadyServlet() {}

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");

        String usernameFromSession = SessionUtils.getUsername(request);
        String userTypeFromParameter = request.getParameter(USER_TYPE);
        EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());

        synchronized (this) {
            engineManager.setUserReady(true, userTypeFromParameter, usernameFromSession);
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
