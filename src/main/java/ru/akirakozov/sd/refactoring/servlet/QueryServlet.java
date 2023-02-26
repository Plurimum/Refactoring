package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.dao.ProductDao;
import ru.akirakozov.sd.refactoring.model.Product;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {
    private final ProductDao productDao;

    public QueryServlet(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");
        StringBuilder body = new StringBuilder();

        switch (command) {
            case "max" -> {
                body.append(String.format("<h1>Product with max price: </h1>%n"));

                productDao.getMaxPriceProduct().map(this::toHtmlString)
                        .ifPresent(body::append);
            }
            case "min" -> {
                body.append(String.format("<h1>Product with min price: </h1>%n"));

                productDao.getMinPriceProduct().map(this::toHtmlString)
                        .ifPresent(body::append);
            }
            case "sum" -> {
                body.append(String.format("Summary price: %n"));

                productDao.getSumPrice().ifPresent(body::append);
            }
            case "count" -> {
                body.append(String.format("Number of products: %n"));

                productDao.getCountOfProducts().ifPresent(body::append);
            }
            default -> response.getWriter().println("Unknown command: " + command);
        }

        printIntoHtml(response.getWriter(), body.toString());

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

    private String toHtmlString(Product product) {
        return String.format(
                "%s\t%d</br>",
                product.name(),
                product.price()
        );
    }
}
