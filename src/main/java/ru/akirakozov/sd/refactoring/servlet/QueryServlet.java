package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.dao.ProductDao;
import ru.akirakozov.sd.refactoring.model.Product;
import ru.akirakozov.sd.refactoring.utils.HtmlStringBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author akirakozov
 */
public class QueryServlet extends AbstractProductServlet {
    private final ProductDao productDao;

    public QueryServlet(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    protected String doGet(HttpServletRequest request) {
        String command = request.getParameter("command");

        return switch (command) {
            case "max" -> buildHtml(
                        "Product with max price: ",
                        productDao.getMaxPriceProduct()
                                .map(Product::valuesString)
                                .orElse("")
                );
            case "min" -> buildHtml(
                        "Product with min price: ",
                        productDao.getMinPriceProduct().map(Product::valuesString)
                                .orElse("")
                );
            case "sum" -> buildHtml(List.of(
                        "Summary price: ",
                        productDao.getSumPrice().map(String::valueOf)
                                .orElse("")
                ));
            case "count" -> buildHtml(List.of(
                        "Number of products: ",
                        productDao.getCountOfProducts().map(String::valueOf)
                                .orElse("")
                ));
            default -> "Unknown command: " + command;
        };
    }

    private String buildHtml(String header, String lineToBreak) {
        return new HtmlStringBuilder()
                .appendHeader(header)
                .appendWithLineBreak(lineToBreak)
                .build();
    }

    private String buildHtml(List<String> lines) {
        return new HtmlStringBuilder()
                .appendLines(lines)
                .build();
    }
}
