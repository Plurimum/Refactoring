package ru.akirakozov.sd.refactoring.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");

        try (
                Connection connection = DriverManager.getConnection("jdbc:sqlite:test.db");
                Statement statement = connection.createStatement();
        ) {
            String body = "";

            switch (command) {
                case "max" -> {
                    ResultSet resultSet = statement.executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1");
                    body = String.format("<h1>Product with max price: </h1>%n");

                    if (resultSet.next()) {
                        body += String.format(
                                "%s\t%s</br>",
                                resultSet.getString("name"),
                                resultSet.getInt("price")
                        );
                    }

                    resultSet.close();
                }
                case "min" -> {
                    ResultSet resultSet = statement.executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1");
                    body = String.format("<h1>Product with min price: </h1>%n");

                    if (resultSet.next()) {
                        body += String.format(
                                "%s\t%s</br>",
                                resultSet.getString("name"),
                                resultSet.getInt("price")
                        );
                    }

                    resultSet.close();
                }
                case "sum" -> {
                    ResultSet rs = statement.executeQuery("SELECT SUM(price) FROM PRODUCT");
                    body = String.format("Summary price: %n");

                    if (rs.next()) {
                        body += rs.getInt(1);
                    }

                    rs.close();
                }
                case "count" -> {
                    ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM PRODUCT");
                    body = String.format("Number of products: %n");

                    if (rs.next()) {
                        body += rs.getInt(1);
                    }

                    rs.close();
                }
                default -> response.getWriter().println("Unknown command: " + command);
            }

            printIntoHtml(response.getWriter(), body);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private void printIntoHtml(PrintWriter writer, String body) {
        if (body.isBlank()) {
            return;
        }

        writer.println("<html><body>");
        writer.println(body);
        writer.println("</body></html>");
    }
}
