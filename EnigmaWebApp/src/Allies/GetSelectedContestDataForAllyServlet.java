package Allies;

import DataTransferObject.UboatDTO;
import Engine.EngineManager;
import MyExceptions.NoContestSelectionException;
import com.google.gson.Gson;
import common.utils.ServletUtils;
import common.utils.SessionUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "getSelectedContestData", urlPatterns = "/getSelectedContestData")
public class GetSelectedContestDataForAllyServlet extends HttpServlet{
    public GetSelectedContestDataForAllyServlet(){}

        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
            response.setContentType("application/json");
            EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());
            String userNameFromSession = SessionUtils.getUsername(request);

            synchronized (this) {
                try (PrintWriter out = response.getWriter()) {
                        UboatDTO uboatDTO = engineManager.getUboatDTO(userNameFromSession);
                        Gson gson = new Gson();
                        String json = gson.toJson(uboatDTO);
                        out.println(json);
                        out.flush();
                        response.setStatus(HttpServletResponse.SC_OK);
                }
                catch (NoContestSelectionException e){
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                }
            }
        }
}
