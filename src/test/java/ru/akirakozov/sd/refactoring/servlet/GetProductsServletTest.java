package ru.akirakozov.sd.refactoring.servlet;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.akirakozov.sd.refactoring.dao.ProductDao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GetProductsServletTest {
    private final ProductDao productDao;

    public GetProductsServletTest() {
        this.productDao = new ProductDao();
    }

    @AfterEach
    @BeforeEach
    public final void cleanDatabase() {
        productDao.cleanTable();
    }

    @Test
    void testGetSingle() throws IOException {
        productDao.addProduct("milk", 50);

        HttpServletRequest request = mock(HttpServletRequest.class);

        HttpServletResponse response = mock(HttpServletResponse.class);
        when(response.getWriter()).thenReturn(new ResponseWriter());

        new GetProductsServlet(new ProductDao()).doGet(request, response);
        String expected = String.format("<html><body>%nmilk\t50</br>%n</body></html>%n");
        assertEquals(expected, response.getWriter().toString());
    }

    @Test
    void testGetMany() throws IOException {
        productDao.addProduct("milk", 50);
        productDao.addProduct("bread", 30);
        productDao.addProduct("meat", 250);

        HttpServletRequest request = mock(HttpServletRequest.class);

        HttpServletResponse response = mock(HttpServletResponse.class);
        when(response.getWriter()).thenReturn(new ResponseWriter());

        new GetProductsServlet(new ProductDao()).doGet(request, response);
        String expected = String.format("<html><body>%nmilk\t50</br>%nbread\t30</br>%nmeat\t250</br>%n</body></html>%n");
        assertEquals(expected, response.getWriter().toString());
    }
}
