package common.servlets;

import Engine.EngineManager;
import common.utils.ServletUtils;
import common.utils.SessionUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static common.constants.Constants.USERNAME;
import static common.constants.Constants.USER_TYPE;

@WebServlet(name = "Login", urlPatterns = "/loginShortResponse") ///////////
public class LoginServlet extends HttpServlet {
    public LoginServlet() {}

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");

        String usernameFromSession = SessionUtils.getUsername(request);
        EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());

        if (usernameFromSession == null) {

            String userTypeFromParameter = request.getParameter(USER_TYPE);
            String usernameFromParameter = request.getParameter(USERNAME);

            if (usernameFromParameter == null || usernameFromParameter.isEmpty() ||
                    userTypeFromParameter == null || userTypeFromParameter.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
            } else {
                usernameFromParameter = usernameFromParameter.trim();

                synchronized (this) {
                    if (engineManager.isUserExists(usernameFromParameter)) {
                        String errorMessage = "Username " + usernameFromParameter + " already exists. Please enter a different username.";
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getOutputStream().print(errorMessage);
                    } else {
                        request.getSession(true).setAttribute(USERNAME, usernameFromParameter);
                        request.getSession(false).setAttribute(USER_TYPE, userTypeFromParameter);

                        response.setStatus(HttpServletResponse.SC_OK);
                        if(!userTypeFromParameter.equals("Uboat"))
                            engineManager.addUserToMap(userTypeFromParameter, usernameFromParameter);
                    }
                }
            }
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }
}