package Agent;

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


@WebServlet(name = "GetEnigmaMachine", urlPatterns = "/GetEnigmaMachine")
public class GetEnigmaMachineServlet extends HttpServlet {
    public GetEnigmaMachineServlet(){}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());
        String allyNameFromSession = SessionUtils.getAllyName(request);

        if(allyNameFromSession == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }else {
            String fileContentString = engineManager.getFileContentString(allyNameFromSession);
            PrintWriter out = response.getWriter();
            out.println(fileContentString);
            out.flush();
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
