package Uboat.servlets;

import Engine.EngineManager;
import MyExceptions.StringContainInvalidWordsException;
import com.google.gson.Gson;
import common.utils.ServletUtils;
import common.utils.SessionUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "encryptString", urlPatterns = "/encryptString")
public class EncryptStringServlet extends HttpServlet {
    public EncryptStringServlet() {
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());
        BufferedReader reader = request.getReader();
        Gson gson = new Gson();
        String stringToEncrypt = gson.fromJson(reader, String.class);

        try{
            String userNameFromSession = SessionUtils.getUsername(request);
            String encryptInputString = engineManager.encryptInputString(stringToEncrypt, userNameFromSession);
            PrintWriter out = response.getWriter();
            out.println(encryptInputString);
            out.flush();
            response.setStatus(HttpServletResponse.SC_OK);
        }catch (StringContainInvalidWordsException e){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getOutputStream().print(e.getMessage());
        }
    }
}
