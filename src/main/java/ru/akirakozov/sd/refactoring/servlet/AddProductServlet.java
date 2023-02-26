package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.dao.ProductDao;

import javax.servlet.http.HttpServletRequest;

/**
 * @author akirakozov
 */
public class AddProductServlet extends AbstractProductServlet {
    private final ProductDao productDao;

    public AddProductServlet(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    protected String doGet(HttpServletRequest request) {
        String name = request.getParameter("name");
        long price = Long.parseLong(request.getParameter("price"));

        try {
            productDao.addProduct(name, price);
            return "OK";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
