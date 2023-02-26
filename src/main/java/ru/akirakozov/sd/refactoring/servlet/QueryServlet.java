package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.dao.ProductDao;
import ru.akirakozov.sd.refactoring.model.Product;
import ru.akirakozov.sd.refactoring.utils.HtmlStringBuilder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
        HtmlStringBuilder htmlStringBuilder = new HtmlStringBuilder();

        switch (command) {
            case "max" -> {
                htmlStringBuilder.appendHeader("Product with max price: ");
                productDao.getMaxPriceProduct().map(Product::valuesString)
                        .ifPresent(htmlStringBuilder::appendWithLineBreak);
            }
            case "min" -> {
                htmlStringBuilder.appendHeader("Product with min price: ");
                productDao.getMinPriceProduct().map(Product::valuesString)
                        .ifPresent(htmlStringBuilder::appendWithLineBreak);
            }
            case "sum" -> {
                htmlStringBuilder.appendNewLineString("Summary price: ");
                productDao.getSumPrice().map(String::valueOf)
                        .ifPresent(htmlStringBuilder::appendNewLineString);
            }
            case "count" -> {
                htmlStringBuilder.appendNewLineString("Number of products: ");
                productDao.getCountOfProducts().map(String::valueOf)
                        .ifPresent(htmlStringBuilder::appendNewLineString);
            }
            default -> {  // TODO: fix it
                response.getWriter().println("Unknown command: " + command);

                response.setContentType("text/html");
                response.setStatus(HttpServletResponse.SC_OK);
                return;
            }
        }

        response.getWriter().println(htmlStringBuilder.build());

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
