package Allies;

import DataTransferObject.UboatDTO;
import Engine.EngineManager;
import com.google.gson.Gson;
import common.utils.ServletUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import usersManager.UsersManager;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

@WebServlet(name = "UboatList", urlPatterns = "/uboatlist")
public class GetUboatListServlet extends HttpServlet {
    public GetUboatListServlet(){}
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());

        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
                Set<UboatDTO> usersList = engineManager.getUboatListDTO();
                String json = gson.toJson(usersList);
                out.println(json);
                out.flush();
        }
    }
}
