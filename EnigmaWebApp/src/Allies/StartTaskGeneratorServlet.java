package Allies;

import DataTransferObject.CodeConfigurationInputDTO;
import Engine.EngineManager;
import com.google.gson.Gson;
import common.utils.ServletUtils;
import common.utils.SessionUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import usersManager.UsersManager;

import java.io.BufferedReader;
import java.io.IOException;

@WebServlet(name = "StartTaskGenerator", urlPatterns = "/startTaskGenerator")
public class StartTaskGeneratorServlet extends HttpServlet {
    public StartTaskGeneratorServlet() {
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());
        String usernameFromSession = SessionUtils.getUsername(request);

        synchronized (this) {
        try {
            engineManager.startTaskGeneratorServlet(usernameFromSession);
                response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            e.printStackTrace();
          }
        }
    }
}

