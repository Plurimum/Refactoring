package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.dao.ProductDao;
import ru.akirakozov.sd.refactoring.model.Product;
import ru.akirakozov.sd.refactoring.utils.HtmlStringBuilder;

import javax.servlet.http.HttpServletRequest;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends AbstractProductServlet {
    private final ProductDao productDao;

    public GetProductsServlet(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    protected String doGet(HttpServletRequest request) {
        try {
            HtmlStringBuilder htmlStringBuilder = new HtmlStringBuilder();
            productDao.findAll().stream()
                    .map(Product::valuesString)
                    .forEach(htmlStringBuilder::appendWithLineBreak);

            return htmlStringBuilder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
