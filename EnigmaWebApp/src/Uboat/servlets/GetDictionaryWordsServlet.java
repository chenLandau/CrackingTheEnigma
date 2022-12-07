package Uboat.servlets;

import DataTransferObject.DictionaryWordsDTO;
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

@WebServlet(name = "DictionaryWords", urlPatterns = "/dictionaryWordsResponse")
public class GetDictionaryWordsServlet extends HttpServlet {
    public GetDictionaryWordsServlet() {}

        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
            response.setContentType("application/json");
            try (PrintWriter out = response.getWriter()) {
                Gson gson = new Gson();
                EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());
                String userNameFromSession = SessionUtils.getUsername(request);
                DictionaryWordsDTO dictionaryWordsDTO = engineManager.getDictionaryWordsDTO(userNameFromSession);
                String json = gson.toJson(dictionaryWordsDTO);
                out.println(json);
                out.flush();
            }
        }
}
