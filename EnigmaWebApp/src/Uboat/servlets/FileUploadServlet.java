package Uboat.servlets;

import Engine.EngineManager;
import common.utils.ServletUtils;
import common.utils.SessionUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import usersManager.UsersManager;

import java.io.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.Scanner;


@WebServlet(name = "FileUpload", urlPatterns = "/FileUploadResponse") ///////////
@MultipartConfig(fileSizeThreshold = 1048576, maxFileSize = 5242880L, maxRequestSize = 26214400L)
public class FileUploadServlet extends HttpServlet {
    public FileUploadServlet() {}

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        Collection<Part> parts = request.getParts();
        StringBuilder fileContent = new StringBuilder();
        Iterator var6 = parts.iterator();

        while(var6.hasNext()) {
            Part part = (Part)var6.next();
            fileContent.append(this.readFromInputStream(part.getInputStream()));
        }
        EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());
        String usernameFromSession = SessionUtils.getUsername(request);

        synchronized (this) {
            try {
                InputStream inputStream = new ByteArrayInputStream(fileContent.toString().getBytes());
                String battleFieldName = engineManager.readCteMachineFromXml(inputStream, usernameFromSession);
                engineManager.addUboatToMap(battleFieldName);
                inputStream.reset();
                String fileContentString = readFromInputStream(inputStream);
                engineManager.setFileContentString(fileContentString, battleFieldName);
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                out.println(e.getMessage());
                out.flush();
            }
        }
    }

    private String readFromInputStream(InputStream inputStream) {
        return (new Scanner(inputStream)).useDelimiter("\\Z").next();
    }
}

