package Agent;

import DataTransferObject.AgentLoginDTO;
import DataTransferObject.CandidateDTO;
import com.google.gson.Gson;
import common.utils.ServletUtils;
import common.utils.SessionUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import Engine.EngineManager;
import usersManager.UsersManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;

import static common.constants.Constants.ALLY_TEAM_NAME;


@WebServlet(name = "AddNewCandidates", urlPatterns = "/AddNewCandidates")
public class AddNewCandidatesServlet extends HttpServlet {
    public AddNewCandidatesServlet() {
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //response.setContentType("application/json");
        String allyNameFromSession = SessionUtils.getAllyName(request);
        if(allyNameFromSession == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }else {
            EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());
            BufferedReader reader = request.getReader();
            Gson gson = new Gson();
            CandidateDTO[] candidatesListDTOS = gson.fromJson(reader, CandidateDTO[].class);

            synchronized (this) {
                engineManager.addNewCandidates(allyNameFromSession, Arrays.asList(candidatesListDTOS));
            }
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }
}


