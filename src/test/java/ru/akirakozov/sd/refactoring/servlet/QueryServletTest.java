package ru.akirakozov.sd.refactoring.servlet;

import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class QueryServletTest extends BaseTest {

    @Test
    void testQuerySingle() throws IOException {
        when(productDao.getMaxPriceProduct()).thenReturn(Optional.of(MILK));
        when(productDao.getMinPriceProduct()).thenReturn(Optional.of(MILK));
        when(productDao.getSumPrice()).thenReturn(Optional.of(50L));
        when(productDao.getCountOfProducts()).thenReturn(Optional.of(1L));

        testCommands("milk\t50</br>", "milk\t50</br>", 50, 1);
    }

    @Test
    void testQueryMany() throws IOException {
        productDao.addProduct("milk", 50);
        productDao.addProduct("bread", 30);
        productDao.addProduct("meat", 250);
        when(productDao.getMaxPriceProduct()).thenReturn(Optional.of(MEAT));
        when(productDao.getMinPriceProduct()).thenReturn(Optional.of(BREAD));
        when(productDao.getCountOfProducts()).thenReturn(Optional.of(3L));
        when(productDao.getSumPrice()).thenReturn(Optional.of(50L + 30 + 250));

        testCommands("meat\t250</br>", "bread\t30</br>", 50L + 30 + 250, 3L);
    }

    private void testCommands(String maxProduct, String minProduct, long sumProduct, long countProduct) throws IOException {
        String max = String.format("<html><body>%n<h1>Product with max price: </h1>%n%s%n</body></html>%n", maxProduct);
        String min = String.format("<html><body>%n<h1>Product with min price: </h1>%n%s%n</body></html>%n", minProduct);
        String sum = String.format("<html><body>%nSummary price: %n%d%n</body></html>%n", sumProduct);
        String count = String.format("<html><body>%nNumber of products: %n%d%n</body></html>%n", countProduct);

        testCommand("max", max);
        testCommand("min", min);
        testCommand("sum", sum);
        testCommand("count", count);
        testCommand("log", String.format("Unknown command: log%n"));
    }

    private void testCommand(String command, String expectedResponse) throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameter("command")).thenReturn(command);
        when(response.getWriter()).thenReturn(new ResponseWriter());

        new QueryServlet(productDao).doGet(request, response);
        assertEquals(expectedResponse, response.getWriter().toString());
    }
}
