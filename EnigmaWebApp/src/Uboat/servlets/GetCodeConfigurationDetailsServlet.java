package Uboat.servlets;

import DataTransferObject.ManualCodeConfigurationDTO;
import Engine.EngineManager;
import com.google.gson.Gson;
import common.utils.ServletUtils;
import common.utils.SessionUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import usersManager.UsersManager;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "CodeConfigurationDetails", urlPatterns = "/CodeConfigurationDetailsResponse")

public class GetCodeConfigurationDetailsServlet extends HttpServlet {
    public GetCodeConfigurationDetailsServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());
            String userNameFromSession = SessionUtils.getUsername(request);
            ManualCodeConfigurationDTO manualCodeConfigurationDTO = engineManager.getManualCodeConfigurationDTO(userNameFromSession);
            String json = gson.toJson(manualCodeConfigurationDTO);
            out.println(json);
            out.flush();
        }
    }
}

