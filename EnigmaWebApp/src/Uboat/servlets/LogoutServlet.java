package Uboat.servlets;

import Engine.EngineManager;
import common.utils.ServletUtils;
import common.utils.SessionUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static common.constants.Constants.*;

@WebServlet(name = "Logout", urlPatterns = "/Logout")
public class LogoutServlet extends HttpServlet {
    public LogoutServlet() {}

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());
        String usernameFromSession = SessionUtils.getUsername(request);
        engineManager.removeUser(usernameFromSession);
        request.getSession().removeAttribute(USERNAME);
        request.getSession().removeAttribute(USER_TYPE);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
