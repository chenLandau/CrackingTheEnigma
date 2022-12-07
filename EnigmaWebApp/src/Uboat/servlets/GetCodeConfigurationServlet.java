package Uboat.servlets;

import DataTransferObject.CodeConfigurationOutputDTO;
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

@WebServlet(name = "CodeConfiguration", urlPatterns = "/CodeConfigurationResponse")
public class GetCodeConfigurationServlet extends HttpServlet {
    public GetCodeConfigurationServlet() {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //returning JSON objects, not HTML
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());
            String userNameFromSession = SessionUtils.getUsername(request);
            CodeConfigurationOutputDTO codeConfigurationOutputDTO = engineManager.getCurrentCodeConfigurationOutputDTO(userNameFromSession);
            String json = gson.toJson(codeConfigurationOutputDTO);
            out.println(json);
            out.flush();
        }
    }
}
