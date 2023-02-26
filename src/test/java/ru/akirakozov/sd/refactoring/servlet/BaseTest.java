package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.dao.ProductDao;
import ru.akirakozov.sd.refactoring.model.Product;

import static org.mockito.Mockito.mock;

public class BaseTest {
    protected final ProductDao productDao;

    protected static final Product MILK = new Product("milk", 50);
    protected static final Product BREAD = new Product("bread", 30);
    protected static final Product MEAT = new Product("meat", 250);

    public BaseTest() {
        this.productDao = mock(ProductDao.class);
    }
}
