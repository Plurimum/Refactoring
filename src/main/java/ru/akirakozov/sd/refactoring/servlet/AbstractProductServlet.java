package ru.akirakozov.sd.refactoring.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AbstractProductServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.getWriter().println(doGet(request));
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    protected abstract String doGet(HttpServletRequest request);
}
