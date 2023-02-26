package ru.akirakozov.sd.refactoring.servlet;

import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GetProductsServletTest extends BaseTest {
    @Test
    void testGetSingle() throws IOException {
        when(productDao.findAll()).thenReturn(List.of(MILK));

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(response.getWriter()).thenReturn(new ResponseWriter());

        new GetProductsServlet(productDao).doGet(request, response);

        String expected = String.format("<html><body>%nmilk\t50</br>%n</body></html>%n");
        assertEquals(expected, response.getWriter().toString());
    }

    @Test
    void testGetMany() throws IOException {
        when(productDao.findAll()).thenReturn(List.of(MILK, BREAD, MEAT));

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(response.getWriter()).thenReturn(new ResponseWriter());

        new GetProductsServlet(productDao).doGet(request, response);

        String expected = String.format("<html><body>%nmilk\t50</br>%nbread\t30</br>%nmeat\t250</br>%n</body></html>%n");
        assertEquals(expected, response.getWriter().toString());
    }
}
