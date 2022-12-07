package Uboat.servlets;

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

import DataTransferObject.*;
@WebServlet(name = "MachineDetails", urlPatterns = "/MachineDetailsResponse")

public class GetMachineDetailsServlet extends HttpServlet {
    public GetMachineDetailsServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());
            String userNameFromSession = SessionUtils.getUsername(request);
            MachineSpecificationsDTO machineSpecificationsDTO = engineManager.getMachineSpecificationsDTO(userNameFromSession);
            String json = gson.toJson(machineSpecificationsDTO);
            out.println(json);
            out.flush();
        }
    }
}
