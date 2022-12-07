package Uboat.servlets;

import DataTransferObject.CodeConfigurationInputDTO;
import Engine.EngineManager;
import com.google.gson.Gson;
import common.utils.ServletUtils;
import common.utils.SessionUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;

@WebServlet(name = "InitializeCodeConfiguration", urlPatterns = "/InitializeCodeConfigurationResponse")
public class InitializeCodeConfigurationServlet extends HttpServlet {
    public InitializeCodeConfigurationServlet() {
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        String selectedInitializationType = request.getParameter("Initialization-Type");
        EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());
        String userNameFromSession = SessionUtils.getUsername(request);

        switch (selectedInitializationType) {
            case "MANUAL":
                BufferedReader reader = request.getReader();
                Gson gson = new Gson();
                CodeConfigurationInputDTO codeConfigurationInputDTO = gson.fromJson(reader, CodeConfigurationInputDTO.class);
                engineManager.initializeManualCodeConfiguration(codeConfigurationInputDTO, userNameFromSession);
                break;
            case "AUTOMATIC":
                engineManager.initializeAutomaticallyCodeConfiguration(userNameFromSession);
                break;
        }
    }
}
