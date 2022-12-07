package Agent;

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

@WebServlet(name = "GetIsContestOverConfirmedServlet", urlPatterns = "/GetIsContestOverConfirmed")
public class IsContestOverConfirmedServlet extends HttpServlet {
    public IsContestOverConfirmedServlet(){}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());
        String allyNameFromSession = SessionUtils.getAllyName(request);

        if (allyNameFromSession == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            synchronized (this) {
                PrintWriter out = response.getWriter();
                Boolean isContestOverConfirmed = engineManager.isContestOverConfirmed(allyNameFromSession);
                Gson gson = new Gson();
                String json = gson.toJson(isContestOverConfirmed);
                out.println(json);
                out.flush();
                response.setStatus(HttpServletResponse.SC_OK);
            }
        }
    }
}